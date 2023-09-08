package meet.myo.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import meet.myo.domain.Certified;
import meet.myo.domain.Member;
import meet.myo.domain.authority.CustomOAuth2User;
import meet.myo.dto.response.SignInResponseDto;
import meet.myo.exception.NotFoundException;
import meet.myo.dto.request.auth.DirectSignInRequestDto;
import meet.myo.dto.request.auth.OauthSignInRequestDto;
import meet.myo.dto.request.auth.SignInRequestDto;
import meet.myo.jwt.TokenDto;
import meet.myo.jwt.TokenProvider;
import meet.myo.repository.MemberRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;


/**
 * 인증 서비스
 *
 * TokenProvider: 토큰에 관련된 작업(생성, 파싱) 위임
 * AuthenticationManagerBuilder: UsernamePasswordAuthenticationToken으로 authenticate() -> loadUserByUsername() 호출, 비밀번호를 대조
 * CustomPrincipalDetailService: loadUserByUsername(), loadUserByOauth() 갖고 있는 클래스
 */

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final CustomPrincipalDetailService customPrincipalDetailService;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String ,String> redisTemplate;

    /**
     * 로그인
     */
    public SignInResponseDto signIn(SignInRequestDto dto) {

        // Authentication 객체 획득
        Authentication authentication = customAuthenticate(dto);

        // Security context에 Authentication 객체 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 토큰 셋 발급
        TokenDto tokenSet = createTokenSetByAuthentication(authentication);

        // member 엔티티 영속화
        Member member = memberRepository.findByEmailAndDeletedAtNull(authentication.getName())
                .orElseThrow(NotFoundException::new);

        if (member.getCertified() == Certified.NOT_CERTIFIED) {
            throw new AccessDeniedException("UNCERTIFIED");
        }

        // refresh token 업데이트
        member.updateRefreshToken(tokenSet.getRefreshToken());

        return SignInResponseDto.builder()
                .email(member.getEmail())
                .nickName(member.getNickname())
                .profileImageUrl(member.getProfileImage().getUrl())
                .token(tokenSet)
                .build();
    }

    /**
     * 토큰 리프레시
     */
    public TokenDto tokenRefresh(String refreshToken) {

        // 토큰의 유효성 체크
        if (!tokenProvider.validateToken(refreshToken, false)) {
            log.warn("Invalid refresh token excepted");
            throw new AccessDeniedException("INVALID_TOKEN");
        }

        Authentication authentication = tokenProvider.getAuthentication(refreshToken, false);

        Member member = memberRepository.findByEmailAndDeletedAtNull(authentication.getName())
                .orElseThrow(NotFoundException::new);

        if (!refreshToken.equals(member.getRefreshToken())) {
            log.warn("Not matched refresh token");
            throw new AccessDeniedException("INVALID_TOKEN");
        }

        // 토큰 셋 발급, 엔티티에 ref token 업데이트
        TokenDto tokenSet = createTokenSetByAuthentication(authentication);
        member.updateRefreshToken(tokenSet.getRefreshToken());

        return tokenSet;
    }

    /**
     * 로그아웃(토큰 만료)
     */
    public void signOut(String header) {
        String token = resolveToken(header);

        if (!tokenProvider.validateToken(token, true)) {
            throw new AccessDeniedException("INVALID_TOKEN");
        }

        Long expiration = tokenProvider.getExpiration(token, true);

        //Redis Cache에 저장
        redisTemplate.opsForValue().set(token, "logout", expiration, TimeUnit.MILLISECONDS);

        //리프레쉬 토큰 삭제
        memberRepository.findByIdAndDeletedAtNull(tokenProvider.getMemberId(token)).orElseThrow(NotFoundException::new)
                .updateRefreshToken(null);
    }

    /**
     * 로그인 요청 dto를 Authentication 객체로 변환
     */
    private <T extends SignInRequestDto> Authentication customAuthenticate(T dto) {

        Authentication authentication = null;

        if (dto instanceof DirectSignInRequestDto directSignInRequestDto) {
            // 인증된 Authentication 객체 획득(authentication manager가 loadByUserName 호출)
            authentication =  authenticationManagerBuilder.getObject().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            directSignInRequestDto.getEmail(), directSignInRequestDto.getPassword())
            );

        } else if (dto instanceof OauthSignInRequestDto oauthSignInRequestDto) {

            // OAuth 버전의 loadByUserName
            CustomOAuth2User user = customPrincipalDetailService.loadUserByOauth(
                    oauthSignInRequestDto.getOauthType(), oauthSignInRequestDto.getOauthId());

            // Authentication 객체 생성
            authentication = new OAuth2AuthenticationToken(user, user.getAuthorities(), oauthSignInRequestDto.getOauthId());
        }

        return authentication;
    }

    /**
     * Authentication 객체를 받아 Token set으로 변환
     */
    private TokenDto createTokenSetByAuthentication(Authentication authentication) {
        return TokenDto.builder()
                .accessToken(tokenProvider.getAccessToken(authentication))
                .refreshToken(tokenProvider.getRefreshToken(authentication))
                .build();
    }

    private String resolveToken(String token) {
        if (!token.startsWith("Bearer ")) {
            throw new IllegalArgumentException("INVALID_TOKEN");
        }
        return token.substring(7);
    }
}
