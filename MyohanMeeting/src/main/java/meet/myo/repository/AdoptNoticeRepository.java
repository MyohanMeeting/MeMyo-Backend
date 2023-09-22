package meet.myo.repository;

import meet.myo.domain.adopt.notice.AdoptNotice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdoptNoticeRepository extends JpaRepository<AdoptNotice, Long> , AdoptNoticeRepositoryCustom {

    Optional<AdoptNotice> findByIdAndDeletedAtNull(Long noticeId);

    @Query("select an from AdoptNotice an " +
            "join fetch an.member m " +
            "join fetch an.thumbnail th " +
            "left join fetch an.catPictures cp " +
            "left join fetch cp.upload u " +
            "where an.id = :noticeId and an.deletedAt is null")
    Optional<AdoptNotice> findAdoptNoticeWithId(@Param("noticeId") Long noticeId);

    @Query(value = "select an from AdoptNotice  an " +
            "join fetch an.member m " +
            "join fetch m.profileImage " +
            "where m.id = :memberId and an.deletedAt is null",
            countQuery = "select count(an) from AdoptNotice an where an.member.id = :memberId and an.deletedAt is null")
    Page<AdoptNotice> findAdoptNoticeByMemberId(Pageable pageable, @Param("memberId") Long memberId);
}
