package meet.myo.domain.cat;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import meet.myo.domain.BaseAuditingListener;
import meet.myo.domain.Upload;
import meet.myo.domain.adopt.notice.Shelter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cat extends BaseAuditingListener {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String age;

    @Column(nullable = false)
    private String species;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sex sex;

    @Column(nullable = false)
    private Double weight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Neutered neutered;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thumbnail_id")
    @Column(nullable = false)
    private Upload thumbnail;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cat")
    private List<CatPictures> pictures;

    @Column(nullable = false)
    private String healthStatus;

    @Column(nullable = false)
    private String personality;

    @Enumerated(EnumType.STRING)
    private Registered registered;

    private String registNumber;

    private String foundedPlace;

    private String foundedAt;

    @Embedded
    private Shelter shelter;
}
