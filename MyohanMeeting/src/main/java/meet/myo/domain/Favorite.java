package meet.myo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import meet.myo.domain.adopt.notice.AdoptNotice;
import meet.myo.domain.adopt.notice.cat.Cat;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Favorite extends BaseAuditingListener {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private AdoptNotice adoptNotice;

    public static Favorite createFavorite(Member member, AdoptNotice adoptNotice) {
        Favorite favorite = new Favorite();
        favorite.member = member;
        favorite.adoptNotice = adoptNotice;
        return favorite;
    }

    public void delete() {
        super.delete();
    }
}
