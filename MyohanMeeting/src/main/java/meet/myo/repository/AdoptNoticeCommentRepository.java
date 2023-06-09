package meet.myo.repository;

import meet.myo.domain.adopt.notice.AdoptNoticeComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdoptNoticeCommentRepository extends JpaRepository<AdoptNoticeComment, Long> {
}
