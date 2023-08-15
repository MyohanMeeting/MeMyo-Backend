package meet.myo.dto.request.adopt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.openapitools.jackson.nullable.JsonNullable;

@Schema(name = "adoptApplicationForm")
@Getter
public class AdoptApplicationUpdateRequestDto {

    private JsonNullable<Applicant> applicant = JsonNullable.undefined();
    private JsonNullable<Survey> survey = JsonNullable.undefined();
    private JsonNullable<String> content = JsonNullable.undefined();

    @Getter
    public static class Applicant {
        private JsonNullable<String> name = JsonNullable.undefined();
        private JsonNullable<Integer> age = JsonNullable.undefined();
        private JsonNullable<String> gender = JsonNullable.undefined();
        private JsonNullable<String> address = JsonNullable.undefined();
        private JsonNullable<String> phoneNumber = JsonNullable.undefined();
        private JsonNullable<String> job = JsonNullable.undefined();
        private JsonNullable<String> married = JsonNullable.undefined();

    }
    @Getter
    public static class Survey {
        private JsonNullable<String> answer1_1 = JsonNullable.undefined();
        private JsonNullable<String> answer1_2 = JsonNullable.undefined();
        private JsonNullable<String> answer2_1 = JsonNullable.undefined();
        private JsonNullable<String> answer2_2 = JsonNullable.undefined();
        private JsonNullable<String> answer3 = JsonNullable.undefined();
        private JsonNullable<String> answer4 = JsonNullable.undefined();
        private JsonNullable<String> answer5 = JsonNullable.undefined();
        private JsonNullable<String> answer6 = JsonNullable.undefined();
    }
}
