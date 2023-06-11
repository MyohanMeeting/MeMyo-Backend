package meet.myo.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import meet.myo.domain.Member;
import meet.myo.domain.authority.CustomUser;
import meet.myo.repository.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    //이메일로 사용자 정보 조회
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.trace("loadUserByUsername called");
        return memberRepository.findByEmail(email)
                .map(this::createUser)
                .orElseThrow(() -> new UsernameNotFoundException("이메일이 \"" + email + "\"인 회원을 찾을 수 없습니다."));
    }

    // CustomUser로 유저 생성
    private CustomUser createUser(Member member) {
        log.trace("createUser called");
        List<GrantedAuthority> grantedAuthorities = member.getMemberAuthorities()
                .stream().map(authority -> new SimpleGrantedAuthority(authority.toAuthority().getAuthorityName()))
                .collect(Collectors.toList());
        return new CustomUser(member.getEmail(), member.getPassword(), grantedAuthorities, member.getId());
    }
}

