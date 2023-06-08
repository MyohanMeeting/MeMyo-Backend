package meet.myo.domain.adopt.application;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

}
