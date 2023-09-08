package meet.myo.repository;


import meet.myo.domain.adopt.application.AdoptApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdoptApplicationRepository extends JpaRepository<AdoptApplication, Long> {

    // 분양공고에 달린 신청 모두 보기
    @Query(value = "SELECT a FROM AdoptApplication a " +
            "JOIN FETCH a.adoptNotice an " +
            "JOIN FETCH a.member m " +
            "JOIN FETCH m.profileImage " +
            "WHERE an.id = :adoptNoticeId AND a.deletedAt IS NULL",
            countQuery = "SELECT COUNT(a) FROM AdoptApplication a WHERE a.adoptNotice.id = :adoptNoticeId AND a.deletedAt IS NULL")
    Page<AdoptApplication> findByWithDetails(@Param("adoptNoticeId") Long adoptNoticeId, Pageable pageable);

    // 나의 분양신청 모두 보기
    @Query(value = "SELECT a FROM AdoptApplication a " +
            "JOIN FETCH a.member m " +
            "JOIN FETCH m.profileImage " +
            "WHERE a.member.id = :memberId AND a.deletedAt IS NULL",
            countQuery = "SELECT COUNT(a) FROM AdoptApplication a WHERE a.member.id = :memberId AND a.deletedAt IS NULL")
    Page<AdoptApplication> findWithDetailsById(@Param("memberId") Long memberId, Pageable pageable);

    // 분양신청 상세보기
    @Query("SELECT a FROM AdoptApplication a " +
            "JOIN FETCH a.member m " +
            "JOIN FETCH a.member.profileImage " +
            "WHERE a.id =:applicationId AND a.deletedAt IS NULL")
    Optional<AdoptApplication> findApplicationWithDetails(@Param("applicationId") Long applicationId);
}
