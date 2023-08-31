package meet.myo.repository;

import meet.myo.domain.adopt.notice.cat.CatPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CatPictureRepository extends JpaRepository<CatPicture, Long> {
    @Query("select c, u from CatPicture c join fetch Upload u where c.adoptNotice.id = :noticeId")
    List<CatPicture> findByNoticeIdInAndDeletedAtNull(Long noticeId);
}
