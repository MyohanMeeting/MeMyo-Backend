package meet.myo.dto.response.adopt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import meet.myo.domain.adopt.application.Applicant;

@Schema(name = "applicant")
@Getter
public class ApplicantResponseDto {
    private String name;
    private Integer age;
    private String gender;
    private String address;
    private String phoneNumber;
    private String job;
    private String married;

    public static ApplicantResponseDto fromEntity(Applicant entity) {
        ApplicantResponseDto schema = new ApplicantResponseDto();
        schema.name = entity.getName();
        schema.age = entity.getAge();
        schema.gender = entity.getGender().toString();
        schema.address = entity.getAddress();
        schema.phoneNumber = entity.getPhoneNumber();
        schema.job = entity.getJob();
        schema.married = entity.getMarried().toString();
        return schema;
    }
}
