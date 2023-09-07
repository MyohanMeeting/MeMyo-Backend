package meet.myo.repository;

import meet.myo.domain.Member;
import meet.myo.domain.OauthType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m join fetch m.profileImage where m.id = :memberId and m.deletedAt is null")
    Optional<Member> findMemberWithId(@Param("memberId") Long memberId);

    Optional<Member> findByIdAndDeletedAtNull(Long memberId);

    Optional<Member> findByEmailAndDeletedAtNull(String email);

    Optional<Member> findByNicknameAndDeletedAtNull(String nickname);

    @Query("select m from Member m " +
            "where m.oauth.oauthType = :oauthType " +
            "and m.oauth.oauthId = :oauthId " +
            "and m.deletedAt is NULL")
    Optional<Member> findByOauth(OauthType oauthType, String oauthId);
}
