package meet.myo.repository;

import meet.myo.domain.EmailCertification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailCertificationRepository extends JpaRepository<EmailCertification, Long> {
    // select * from member where member_id = 1 order by member_id desc limit 1;
    Optional<EmailCertification> findLatestByMemberIdAndUuidAndDeletedAtNull(Long memberId, String uuid);

}
