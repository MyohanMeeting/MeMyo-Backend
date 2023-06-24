package meet.myo.repository;

import meet.myo.domain.Upload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UploadRepository extends JpaRepository<Upload, Long> {

    Optional<Upload> findByIdAndDeletedAtNull(Long uploadId);

}
