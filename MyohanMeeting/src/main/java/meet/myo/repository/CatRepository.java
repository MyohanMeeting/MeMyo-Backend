package meet.myo.repository;

import meet.myo.domain.cat.Cat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CatRepository extends JpaRepository<Cat, Long> {
    Optional<Cat> findByIdAndDeletedAtNull(Long catId);
}
