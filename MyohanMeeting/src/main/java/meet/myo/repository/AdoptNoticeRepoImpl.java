package meet.myo.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import meet.myo.domain.adopt.notice.AdoptNotice;
import meet.myo.domain.adopt.notice.QAdoptNotice;
import meet.myo.search.AdoptNoticeSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AdoptNoticeRepoImpl implements AdoptNoticeRepositoryCustom {

    private final EntityManager em;

    @Override
    public Page<AdoptNotice> findByDeletedAtNull(Pageable pageable, AdoptNoticeSearch search) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QAdoptNotice qAdoptNotice = QAdoptNotice.adoptNotice;
        List<OrderSpecifier<?>> orderSpecifiers = getSortExpressions(qAdoptNotice, search.getSort());

        List<AdoptNotice> content = queryFactory
                .selectFrom(qAdoptNotice)
                .where(
                        qAdoptNotice.deletedAt.isNull(),
                        titleContains(qAdoptNotice, search.getTitle()),
                        contentContains(qAdoptNotice, search.getContent()),
                        authorNameContains(qAdoptNotice, search.getAuthorName()),
                        noticeStatusContains(qAdoptNotice, search.getNoticeStatus()),
                        catNameContains(qAdoptNotice, search.getCatName()),
                        catSpeciesContains(qAdoptNotice, search.getCatSpecies()),
                        catSexContains(qAdoptNotice, search.getCatSex()),
                        neuteredContains(qAdoptNotice, search.getNeutered()),
                        cityContains(qAdoptNotice, search.getCity()),
                        shelterNameContains(qAdoptNotice, search.getShelterName())
                )
                .orderBy(orderSpecifiers.toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(qAdoptNotice)
                .where(qAdoptNotice.deletedAt.isNull(),
                        titleContains(qAdoptNotice, search.getTitle()),
                        contentContains(qAdoptNotice, search.getContent()),
                        authorNameContains(qAdoptNotice, search.getAuthorName()),
                        noticeStatusContains(qAdoptNotice, search.getNoticeStatus()),
                        catNameContains(qAdoptNotice, search.getCatName()),
                        catSpeciesContains(qAdoptNotice, search.getCatSpecies()),
                        catSexContains(qAdoptNotice, search.getCatSex()),
                        neuteredContains(qAdoptNotice, search.getNeutered()),
                        cityContains(qAdoptNotice, search.getCity()),
                        shelterNameContains(qAdoptNotice, search.getShelterName()))
                .fetchCount();
        return new PageImpl<>(content, pageable, total);

    }

    private List<OrderSpecifier<?>> getSortExpressions(QAdoptNotice qAdoptNotice, String sort) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        if (sort != null && !sort.isEmpty()) {
            String[] sortItems = sort.split(",");
            for (String sortItem : sortItems) {
                String[] parts = sortItem.split("-");
                String field = parts[0];
                String sortOrder = parts.length > 1 ? parts[1] : "asc";

                OrderSpecifier<?> orderSpecifier = null;

                if ("createdAt".equalsIgnoreCase(field)) {
                    orderSpecifier = createForCreatedAt(qAdoptNotice, sortOrder);
                } else if ("applicationCount".equalsIgnoreCase(field)) {
                    orderSpecifier = createForApplicationCount(qAdoptNotice, sortOrder);
                } else if ("commentCount".equalsIgnoreCase(field)) {
                    orderSpecifier = createForCommentCount(qAdoptNotice, sortOrder);
                }
                if (orderSpecifier != null) {
                    orderSpecifiers.add(orderSpecifier);
                }
            }
        }
        return orderSpecifiers;
    }

    // 정렬조건 메서드
    private OrderSpecifier<?> createForCreatedAt(QAdoptNotice qAdoptNotice, String sortOrder) {
        return "desc".equalsIgnoreCase(sortOrder) ? qAdoptNotice.createdAt.desc() : qAdoptNotice.createdAt.asc();
    }

    private OrderSpecifier<?> createForApplicationCount(QAdoptNotice qAdoptNotice, String sortOrder) {
        return "desc".equalsIgnoreCase(sortOrder) ? qAdoptNotice.applicationCount.desc() : qAdoptNotice.applicationCount.asc();
    }

    private OrderSpecifier<?> createForCommentCount(QAdoptNotice qAdoptNotice, String sortOrder) {
        return "desc".equalsIgnoreCase(sortOrder) ? qAdoptNotice.commentCount.desc() : qAdoptNotice.commentCount.asc();
    }

    // 검색조건 메서드
    private Predicate shelterNameContains(QAdoptNotice qAdoptNotice, String shelterName) {
        if (shelterName != null && !shelterName.isEmpty()) {
            return qAdoptNotice.shelter.name.stringValue().contains(shelterName);
        }
        return null;
    }

    private Predicate cityContains(QAdoptNotice qAdoptNotice, String city) {
        if (city != null && !city.isEmpty()) {
            return qAdoptNotice.shelter.city.stringValue().contains(city);
        }
        return null;
    }

    private Predicate neuteredContains(QAdoptNotice qAdoptNotice, String neutered) {
        if (neutered != null && !neutered.isEmpty()) {
            return qAdoptNotice.cat.neutered.stringValue().contains(neutered);
        }
        return null;
    }

    private Predicate catSexContains(QAdoptNotice qAdoptNotice, String catSex) {
        if (catSex != null && !catSex.isEmpty()) {
            return qAdoptNotice.cat.sex.stringValue().contains(catSex);
        }
        return null;
    }

    private Predicate catSpeciesContains(QAdoptNotice qAdoptNotice, String catSpecies) {
        if (catSpecies != null && !catSpecies.isEmpty()) {
            return qAdoptNotice.cat.species.contains(catSpecies);
        }
        return null;
    }

    private Predicate catNameContains(QAdoptNotice qAdoptNotice, String catName) {
        if (catName != null && !catName.isEmpty()) {
            return qAdoptNotice.cat.name.contains(catName);
        }
        return null;
    }

    private Predicate noticeStatusContains(QAdoptNotice qAdoptNotice, String noticeStatus) {
        if (noticeStatus != null && !noticeStatus.isEmpty()) {
            return qAdoptNotice.noticeStatus.stringValue().contains(noticeStatus);
        }
        return null;
    }

    private Predicate authorNameContains(QAdoptNotice qAdoptNotice, String authorName) {
        if (authorName != null && !authorName.isEmpty()) {
            return qAdoptNotice.member.nickname.contains(authorName);
        }
        return null;
    }


    private BooleanExpression titleContains(QAdoptNotice qAdoptNotice, String title) {
        if (title != null && !title.isEmpty()) {
            return qAdoptNotice.title.contains(title);
        }
        return null;
    }

    private Predicate contentContains(QAdoptNotice qAdoptNotice, String content) {
        if (content != null && !content.isEmpty()) {
            return qAdoptNotice.content.contains(content);
        }
        return null;
    }
}


