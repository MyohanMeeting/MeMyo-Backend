package meet.myo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import meet.myo.domain.cat.Cat;

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
    @Column(nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cat_id")
    @Column(nullable = false)
    private Cat cat;

    public static Favorite createFavorite(Member member, Cat cat) {
        Favorite favorite = new Favorite();
        favorite.member = member;
        favorite.cat = cat;
        return favorite;
    }
}
