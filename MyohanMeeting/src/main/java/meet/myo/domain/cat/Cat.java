package meet.myo.domain.cat;

import jakarta.persistence.*;
import lombok.*;
import meet.myo.domain.BaseAuditingListener;
import meet.myo.domain.Upload;
import meet.myo.domain.adopt.notice.AdoptNotice;
import meet.myo.domain.adopt.notice.Shelter;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cat extends BaseAuditingListener {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "cat")
    private AdoptNotice adoptNotice;

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
    private Upload thumbnail;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cat")
    private List<CatPicture> pictures;

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

    @Builder
    Cat(String name, String age, String species, Sex sex, Double weight, Neutered neutered, Upload thumbnail, String healthStatus, String personality, Registered registered, String registNumber, String foundedPlace, String foundedAt, Shelter shelter) {
        this.name = name;
        this.age = age;
        this.species = species;
        this.sex = sex;
        this.weight = weight;
        this.neutered = neutered;
        this.thumbnail = thumbnail;
        this.healthStatus = healthStatus;
        this.personality = personality;
        this.registered = registered;
        this.registNumber = registNumber;
        this.foundedPlace = foundedPlace;
        this.foundedAt = foundedAt;
        this.shelter = shelter;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateAge(String age) {
        this.age = age;
    }

    public void updateSpecies(String species) {
        this.species = species;
    }

    public void updateSex(Sex sex) {
        this.sex = sex;
    }

    public void updateWeight(Double weight) {
        this.weight = weight;
    }

    public void updateNeutered(Neutered neutered) {
        this.neutered = neutered;
    }

    public void updateThumbnail(Upload thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void updateHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public void updatePersonality(String personality) {
        this.personality = personality;
    }

    public void updateRegistered(Registered registered) {
        this.registered = registered;
    }

    public void updateRegistNumber(String registNumber) {
        this.registNumber = registNumber;
    }

    public void updateFoundedPlace(String foundedPlace) {
        this.foundedPlace = foundedPlace;
    }

    public void updateFoundedAt(String foundedAt) {
        this.foundedAt = foundedAt;
    }

    public void updateShelter(Shelter shelter) {
        this.shelter = shelter;
    }
}
