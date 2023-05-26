package meet.myo.domain.cat;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import meet.myo.domain.adopt.Shelter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Cat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cat_id")
    private Long id;

    private String name;

    private String age;

    private LocalDate foundedAt;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    private Double weight;

    @Enumerated(EnumType.STRING)
    private Neutered neutered;

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
