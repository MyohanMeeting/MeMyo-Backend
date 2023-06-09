package meet.myo.domain.adopt.application;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Applicant {

    @Column(name = "applicant_name", nullable = false)
    private String name;

    @Column(name = "applicant_age", nullable = false)
    private Integer age;

    @Column(name = "applicant_gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "applicant_address")
    private String address;

    @Column(name = "applicant_phone")
    private String phoneNumber;

    @Column(name = "applicant_job")
    private String job;

    @Column(name = "applicant_married")
    @Enumerated(EnumType.STRING)
    private Married married; //MARRIED, UNMARRIED

    // TODO: 개인정보를 어떻게 다룰지 생각하기
    public void updateName(String name) {
        this.name = name;
    }
    public void updateAge(Integer age) {
        this.age = age;
    }
    public void updateGender(Gender gender) {
        this.gender = gender;
    }
    public void updateAddress(String address) {
        this.address = address;
    }
    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void updateJob(String job) {
        this.job = job;
    }
    public void updateMarried(Married married) {
        this.married = married;
    }

}
