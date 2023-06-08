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

    private String name;

    private String age;

    private LocalDate foundedAt;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    private Double weight;

    @Enumerated(EnumType.STRING)
    private Neutered neutered;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thumbnail_id")
    private Upload thumbnail;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cat")
    private List<CatPictures> pictures;

    private String healthStatus;

    private String species;

    private String personality;

    private String registNumber;

    @Enumerated(EnumType.STRING)
    private Registered registered;

    private String foundedPlace;

    @Embedded
    private Shelter shelter;
}
