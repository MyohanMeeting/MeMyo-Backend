package meet.myo.repository;

import com.querydsl.core.types.OrderSpecifier;
import meet.myo.domain.adopt.notice.AdoptNotice;
import meet.myo.search.AdoptNoticeSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


import java.util.List;

//TODO: QueryDsl용 레포지토리 커스텀 인터페이스
// 서치가 잦고 동적 쿼리 생성이 필요한 엔티티일 것 같아서 QueryDsl용 레포지토리를 미리 만들어둡니당
@Repository
public interface AdoptNoticeRepositoryCustom {
    Page<AdoptNotice> findByDeletedAtNull(Pageable pageable, AdoptNoticeSearch search);

}
