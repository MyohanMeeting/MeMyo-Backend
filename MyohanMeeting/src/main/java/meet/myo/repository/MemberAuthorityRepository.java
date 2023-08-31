package meet.myo.repository;

import meet.myo.domain.authority.MemberAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberAuthorityRepository extends JpaRepository<MemberAuthority, Long> {
    List<MemberAuthority> findByMemberId(Long memberId);
}
