package meet.myo.dto.response.adopt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import meet.myo.domain.adopt.application.Survey;

@Schema(name = "survey")
@Getter
public class SurveyResponseDto {
    private String answer1_1;
    private String answer1_2;
    private String answer2_1;
    private String answer2_2;
    private String answer3;
    private String answer4;
    private String answer5;
    private String answer6;

    public static SurveyResponseDto fromEntity(Survey entity) {
        SurveyResponseDto schema = new SurveyResponseDto();

        schema.answer1_1 = entity.getSurveyAnswer1of1().toString();
        schema.answer1_2 = entity.getSurveyAnswer1of2();
        schema.answer2_1 = entity.getSurveyAnswer2of1().toString();
        schema.answer2_2 = entity.getSurveyAnswer2of2();
        schema.answer3 = entity.getSurveyAnswer3();
        schema.answer4 = entity.getSurveyAnswer4().toString();
        schema.answer5 = entity.getSurveyAnswer5();
        schema.answer6 = entity.getSurveyAnswer6().toString();

        return schema;
    }
}
