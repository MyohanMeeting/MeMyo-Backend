package meet.myo.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import meet.myo.domain.Member;
import meet.myo.domain.OauthType;
import meet.myo.domain.authority.CustomOAuth2User;
import meet.myo.domain.authority.CustomUser;
import meet.myo.exception.NotFoundException;
import meet.myo.repository.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CustomPrincipalDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * username(email)로 Authentication 객체 조회
     * AuthenticationManagerBuilder의 authenticate() 실행 시 호출됨
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmailAndDeletedAtNull(email)
                .map(this::createUser)
                .orElseThrow(() -> new UsernameNotFoundException("이메일이 \"" + email + "\"인 회원을 찾을 수 없습니다."));
    }

    /**
     * OAuth 정보로 Authentication 객체 조회
     * loadByUsername과는 달리 직접 호출해야 함.
     */
    public CustomOAuth2User loadUserByOauth(String oauthType, String oauthId) {
        return memberRepository.findByOauthOauthTypeAndOauthOauthIdAndDeletedAtNull(OauthType.valueOf(oauthType), oauthId)
                .map(this::createOauthUser)
                .orElseThrow(NotFoundException::new);
    }


    private CustomUser createUser(Member member) {
        List<GrantedAuthority> grantedAuthorities = member.getMemberAuthorities()
                .stream().map(authority -> new SimpleGrantedAuthority(authority.toAuthority().getAuthorityName()))
                .collect(Collectors.toList());
        return new CustomUser(
                member.getEmail(),
                member.getPassword() != null ? member.getPassword() : "", // OAuth2 가입으로 비밀번호 없는 경우 blank 처리
                grantedAuthorities,
                member.getId());
    }

    private CustomOAuth2User createOauthUser(Member member) {
        Map<String, Object> attributes = Map.of(
                "email", member.getEmail(),
                "oauthType", member.getOauth().getOauthType().toString(),
                "oauthId", member.getOauth().getOauthId()
        );
        List<GrantedAuthority> grantedAuthorities = member.getMemberAuthorities()
                .stream().map(authority -> new SimpleGrantedAuthority(authority.toAuthority().getAuthorityName()))
                .collect(Collectors.toList());
        return new CustomOAuth2User(grantedAuthorities, attributes, "email", member.getId());
    }
}

