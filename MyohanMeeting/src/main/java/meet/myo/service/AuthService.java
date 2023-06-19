package meet.myo.service;

import io.jsonwebtoken.JwtException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import meet.myo.domain.Member;
import meet.myo.domain.exception.NotFoundException;
import meet.myo.dto.request.SignInRequestDto;
import meet.myo.jwt.TokenDto;
import meet.myo.jwt.TokenProvider;
import meet.myo.repository.MemberRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;

    public TokenDto getToken(SignInRequestDto dto) throws NotFoundException {

        // AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());

        // Token을 통해 인증된 Authentication 객체 획득
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // Security context에 Authentication 객체 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 토큰 셋 발급
        TokenDto tokenSet = createTokenSetByAuthentication(authentication);

        // member 엔티티 영속화
        Member member = memberRepository.findByEmailAndDeletedAtNull(authentication.getName())
                .orElseThrow(NotFoundException::new);

        // refresh token 업데이트
        member.updateRefreshToken(tokenSet.getRefreshToken());

        return tokenSet;
    }

    public TokenDto tokenRefresh(String refreshToken) throws JwtException {
        // 토큰의 유효성 체크
        if (!tokenProvider.validateToken(refreshToken)) {
            log.warn("Invalid refresh token excepted");
            throw new JwtException("INVALID_TOKEN");
        }

        Authentication authentication = tokenProvider.getAuthentication(refreshToken);
        Member member = memberRepository.findByEmailAndDeletedAtNull(authentication.getName())
                .orElseThrow(NotFoundException::new);

        if (!refreshToken.equals(member.getRefreshToken())) {
            log.warn("Not matched refresh token");
            throw new JwtException("INVALID_TOKEN");
        }

        // 토큰 셋 발급, 엔티티에 ref token 업데이트
        TokenDto tokenSet = createTokenSetByAuthentication(authentication);
        member.updateRefreshToken(tokenSet.getRefreshToken());

        return tokenSet;
    }

    private TokenDto createTokenSetByAuthentication(Authentication authentication) {
        return TokenDto.builder()
                .accessToken(tokenProvider.getAccessToken(authentication))
                .refreshToken(tokenProvider.getRefreshToken(authentication))
                .build();
    }
}
