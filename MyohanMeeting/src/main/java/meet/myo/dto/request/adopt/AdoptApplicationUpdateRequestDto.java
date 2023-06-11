package meet.myo.dto.request.adopt;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import meet.myo.dto.schema.ApplicantSchema;
import meet.myo.dto.schema.SurveySchema;

@Getter
public class AdoptApplicationUpdateRequestDto {

    private ApplicantSchema applicant;
    private SurveySchema survey;
    private String content;
}
