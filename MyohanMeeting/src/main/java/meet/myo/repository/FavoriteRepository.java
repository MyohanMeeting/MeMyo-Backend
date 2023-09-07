package meet.myo.repository;

import meet.myo.domain.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByIdAndDeletedAtNull(Long id);
    Optional<Favorite> findByMemberIdAndAdoptNoticeIdAndDeletedAtNull(Long memberId, Long adoptNoticeId);

    @Query(value = "select f from Favorite f " +
            "join fetch f.adoptNotice an " +
            "join fetch f.member m " +
            "join fetch an.member anm " +
            "join fetch an.thumbnail ant " +
            "where f.member.id = :memberId and f.deletedAt is null",
            countQuery = "select count(f) FROM Favorite f WHERE f.member.id = :memberId and f.deletedAt is null")
    Page<Favorite> findFavoriteByMemberId(@Param("memberId") Long memberId, Pageable pageable);
}
