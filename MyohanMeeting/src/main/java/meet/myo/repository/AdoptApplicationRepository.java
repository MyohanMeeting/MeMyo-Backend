package meet.myo.repository;


import meet.myo.domain.adopt.application.AdoptApplication;
import meet.myo.domain.adopt.notice.AdoptNotice;
import org.springframework.data.domain.Page;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface AdoptApplicationRepository extends JpaRepository<AdoptApplication, Long> {

    Page<AdoptApplication> findByAdoptNoticeAndDeletedAtNull(AdoptNotice adoptNotice, org.springframework.data.domain.Pageable pageable);

    Page<AdoptApplication> findByMemberIdAndDeletedAtNull(Long memberId, org.springframework.data.domain.Pageable pageable);
}
