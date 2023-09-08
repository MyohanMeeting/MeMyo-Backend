package meet.myo.repository;

import meet.myo.domain.adopt.notice.AdoptNoticeComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdoptNoticeCommentRepository extends JpaRepository<AdoptNoticeComment, Long> {

    Optional<AdoptNoticeComment> findByIdAndDeletedAtNull(Long noticeCommentId);

    @Query("SELECT anc FROM AdoptNoticeComment anc " +
            "JOIN FETCH anc.member m " +
            "JOIN FETCH m.profileImage WHERE anc.adoptNotice.id = :noticeId and anc.deletedAt is null")
    List<AdoptNoticeComment> findByAdoptNoticeIdAndDeletedAtNull(@Param("noticeId") Long noticeId);
}
