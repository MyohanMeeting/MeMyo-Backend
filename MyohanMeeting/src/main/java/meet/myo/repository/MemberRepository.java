package meet.myo.repository;

import meet.myo.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByIdAndDeletedAtNull(Long memberId);

    Optional<Member> findByEmailAndDeletedAtNull(String email);

    Optional<Member> findByNickNameAndDeletedAtNull(String nickName);

    Optional<Member> findByOauthTypeAndOauthCodeAndDeletedAtNull(String oauthType, String oauthCode);

}
