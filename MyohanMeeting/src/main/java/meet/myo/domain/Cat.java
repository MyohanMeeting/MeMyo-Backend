package meet.myo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Cat {
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

    private List<Upload> picture;

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
