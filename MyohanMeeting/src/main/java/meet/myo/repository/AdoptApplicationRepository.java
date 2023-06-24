package meet.myo.repository;


import meet.myo.domain.adopt.application.AdoptApplication;
import meet.myo.domain.adopt.notice.AdoptNotice;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdoptApplicationRepository extends JpaRepository<AdoptApplication, Long> {

    Page<AdoptApplication> findByAdoptNoticeIdAndDeletedAtNull(AdoptNotice adoptNotice, org.springframework.data.domain.Pageable pageable);

    Page<AdoptApplication> findByMemberIdAndDeletedAtNull(Long memberId, org.springframework.data.domain.Pageable pageable);


}
