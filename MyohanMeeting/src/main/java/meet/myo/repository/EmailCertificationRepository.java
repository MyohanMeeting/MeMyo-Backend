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

    @Query("select e from EmailCertification e " +
            "where e.certCode = :certCode " +
            "and e.email = :email " +
            "and e.deletedAt is NULL " +
            "order by e.id desc " +
            "limit 1")
    Optional<EmailCertification> findByCertCodeAndEmail(@Param("certCode")String certCode,
                                                        @Param("email")String email);
}
