package meet.myo.repository;

import meet.myo.domain.EmailCertification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmailCertificationRepository extends JpaRepository<EmailCertification, Long> {
    @Query("SELECT ec FROM EmailCertification ec " +
            "WHERE ec.member.id = :memberId " +
            "AND ec.certCode = :certCode " +
            "AND ec.deletedAt IS NULL")
    Optional<EmailCertification> findByMemberIdAndCertCode(@Param("memberId") Long memberId,
                                                       @Param("certCode") String certCode);

}
