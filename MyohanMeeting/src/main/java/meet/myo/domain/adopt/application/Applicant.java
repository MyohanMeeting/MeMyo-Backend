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

    @NonNull
    @Column(name = "applicant_name")
    private String name;

    @NonNull
    @Column(name = "applicant_age")
    private Integer age;

    @NonNull
    @Column(name = "applicant_gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NonNull
    @Column(name = "applicant_address")
    private String address;

    @NonNull
    @Column(name = "applicant_phone")
    private String phoneNumber;

    @NonNull
    @Column(name = "applicant_job")
    private String job;

    @NonNull
    @Column(name = "applicant_married")
    @Enumerated(EnumType.STRING)
    private Married married; //MARRIED, UNMARRIED

}
