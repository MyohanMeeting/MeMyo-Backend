package meet.myo.repository;

import meet.myo.domain.adopt.notice.AdoptNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdoptNoticeRepository extends JpaRepository<AdoptNotice, Long> {

    Optional<Object> findByAdoptNoticeId(Long noticeId);
}
