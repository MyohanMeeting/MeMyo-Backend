package meet.myo.dto.request.adopt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(name = "adoptApplicationForm")
@Getter
public class AdoptApplicationRequestDto {

    private Long noticeId;
    private Applicant applicant;
    private Survey survey;
    private String content;

    @Getter
    public static class Applicant {
        private String name;
        private Integer age;
        private String gender;
        private String address;
        private String phoneNumber;
        private String job;
        private String married;

    }
    @Getter
    public static class Survey {
        private String answer1_1;
        private String answer1_2;
        private String answer2_1;
        private String answer2_2;
        private String answer3;
        private String answer4;
        private String answer5;
        private String answer6;
    }
}


