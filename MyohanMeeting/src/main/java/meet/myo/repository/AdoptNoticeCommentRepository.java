package meet.myo.repository;

import meet.myo.domain.adopt.notice.AdoptNoticeComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdoptNoticeCommentRepository extends JpaRepository<AdoptNoticeComment, Long> {

    Optional<AdoptNoticeComment> findByIdAndDeletedAtNull(Long noticeCommentId);

    List<AdoptNoticeComment> findByAdoptNoticeIdAndDeletedAtNull(Long noticeId);
}
