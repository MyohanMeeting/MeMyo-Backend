package meet.myo.repository;

import meet.myo.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByIdAndDeletedAtNull(Long memberId);

    // findOneBy
    Optional<Member> findByEmailAndDeletedAtNull(String email);

    // findOneBy
    Optional<Member> findByNickNameAndDeletedAtNull(String nickName);

    Member findByNickName(String nickName);

}
