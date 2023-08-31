package meet.myo.repository;


import meet.myo.domain.adopt.application.AdoptApplication;
import meet.myo.domain.adopt.notice.AdoptNotice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdoptApplicationRepository extends JpaRepository<AdoptApplication, Long> {

    Page<AdoptApplication> findByAdoptNoticeIdAndDeletedAtNull(Long adoptNoticeId, Pageable pageable);

    Page<AdoptApplication> findByMemberIdAndDeletedAtNull(Long memberId, Pageable pageable);

    Optional<AdoptApplication> findByIdAndDeletedAtNull(Long applicationId);
}
