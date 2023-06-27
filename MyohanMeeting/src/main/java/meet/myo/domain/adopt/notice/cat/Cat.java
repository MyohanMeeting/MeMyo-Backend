package meet.myo.domain.adopt.notice.cat;

import jakarta.persistence.*;
import lombok.*;
import meet.myo.domain.Upload;
import meet.myo.domain.adopt.notice.Shelter;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cat {

    @Column(name = "cat_name")
    private String name;

    @Column(name = "cat_age")
    private String age;

    @Column(name = "cat_species")
    private String species;

    @Enumerated(EnumType.STRING)
    @Column(name = "cat_sex")
    private Sex sex;

    @Column(name = "cat_weight")
    private Double weight;

    @Enumerated(EnumType.STRING)
    @Column(name = "cat_neutered")
    private Neutered neutered;

    @Column(name = "cat_healthStatus", columnDefinition = "TEXT")
    private String healthStatus;

    @Column(name = "cat_personality", columnDefinition = "TEXT")
    private String personality;

    @Enumerated(EnumType.STRING)
    @Column(name = "cat_registered")
    private Registered registered;

    @Column(name = "cat_registNumber")
    private String registNumber;

    @Column(name = "cat_foundedPlace")
    private String foundedPlace;

    @Column(name = "cat_foundedAt")
    private String foundedAt;

    @Builder
    Cat(String name, String age, String species, Sex sex, Double weight, Neutered neutered, String healthStatus, String personality, Registered registered, String registNumber, String foundedPlace, String foundedAt) {
        this.name = name;
        this.age = age;
        this.species = species;
        this.sex = sex;
        this.weight = weight;
        this.neutered = neutered;
        this.healthStatus = healthStatus;
        this.personality = personality;
        this.registered = registered;
        this.registNumber = registNumber;
        this.foundedPlace = foundedPlace;
        this.foundedAt = foundedAt;
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

}
