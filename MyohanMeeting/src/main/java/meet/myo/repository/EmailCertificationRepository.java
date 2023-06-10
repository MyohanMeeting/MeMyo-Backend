package meet.myo.repository;

import meet.myo.domain.EmailCertification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailCertificationRepository extends JpaRepository<EmailCertification, Long> {
    Optional<EmailCertification> findLatestByMemberId(Long memberId);

}
