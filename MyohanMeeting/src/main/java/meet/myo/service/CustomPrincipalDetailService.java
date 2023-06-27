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

    //이메일로 사용자 정보 조회
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.trace("loadUserByUsername called");
        return memberRepository.findByEmailAndDeletedAtNull(email)
                .map(this::createUser)
                .orElseThrow(() -> new UsernameNotFoundException("이메일이 \"" + email + "\"인 회원을 찾을 수 없습니다."));
    }

    // Oauth 사용자 조회
    public CustomOAuth2User loadUserByOauth(String oauthType, String oauthId) {
        return memberRepository.findByOauthOauthTypeAndOauthOauthIdAndDeletedAtNull(OauthType.valueOf(oauthType), oauthId)
                .map(this::createOauthUser)
                .orElseThrow(NotFoundException::new);
    }

    // CustomUser로 유저 생성
    private CustomUser createUser(Member member) {
        List<GrantedAuthority> grantedAuthorities = member.getMemberAuthorities()
                .stream().map(authority -> new SimpleGrantedAuthority(authority.toAuthority().getAuthorityName()))
                .collect(Collectors.toList());
        return new CustomUser(member.getEmail(), member.getPassword(), grantedAuthorities, member.getId());
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

