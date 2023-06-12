package meet.myo.dto.schema;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(name = "survey")
@Getter
public class SurveySchema {
    private String answer1_1;
    private String answer1_2;
    private String answer2_1;
    private String answer2_2;
    private String answer3;
    private String answer4;
    private String answer5;
    private String answer6;
}
