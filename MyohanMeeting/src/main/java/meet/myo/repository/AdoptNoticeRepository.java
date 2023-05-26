package meet.myo.repository;

import meet.myo.domain.adopt.AdoptNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdoptNoticeRepository extends JpaRepository<AdoptNotice, Long> {

}
