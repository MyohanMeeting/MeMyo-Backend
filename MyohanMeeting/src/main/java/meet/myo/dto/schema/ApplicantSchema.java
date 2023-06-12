package meet.myo.dto.schema;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(name = "applicant")
@Getter
public class ApplicantSchema {
    private String name;
    private Integer age;
    private String gender;
    private String address;
    private String phoneNumber;
    private String job;
    private String married;
}
