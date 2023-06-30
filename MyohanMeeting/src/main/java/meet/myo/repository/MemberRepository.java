package meet.myo.repository;

import meet.myo.domain.Member;
import meet.myo.domain.OauthType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByIdAndDeletedAtNull(Long memberId);

    // findOneBy
    Optional<Member> findByEmailAndDeletedAtNull(String email);
    Optional<Member> findByOauthOauthTypeAndOauthOauthIdAndDeletedAtNull(OauthType oauthType, String oauthId);

    // findOneBy
    Optional<Member> findByNicknameAndDeletedAtNull(String nickname);

}
