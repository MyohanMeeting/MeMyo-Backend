package meet.myo.dto.request.adopt;

import lombok.Getter;
import meet.myo.domain.adopt.application.Married;
import meet.myo.dto.schema.ApplicantSchema;
import meet.myo.dto.schema.SurveySchema;

@Getter
public class AdoptApplicationCreateRequestDto {

    private Long noticeId;
    private ApplicantSchema applicant;
    private SurveySchema survey;
    private String content;

    public static class ApplicantDto {
        private String name;
        private Integer age;
        private String gender;
        private String address;
        private String phoneNumber;
        private String job;
        private String married;

    }

    public static class SurveyDto {
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


