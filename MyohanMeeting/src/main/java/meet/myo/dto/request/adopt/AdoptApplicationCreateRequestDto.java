package meet.myo.dto.request.adopt;

import lombok.Getter;
import meet.myo.dto.schema.ApplicantSchema;
import meet.myo.dto.schema.SurveySchema;

@Getter
public class AdoptApplicationCreateRequestDto {

    private Long noticeId;
    private ApplicantSchema applicant;
    private SurveySchema survey;
    private String content;
}