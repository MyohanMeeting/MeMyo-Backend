package meet.myo.dto.request.adopt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.List;


@Schema(name = "adoptNoticeForm")
@Getter
public class AdoptNoticeUpdateRequestDto {

    private JsonNullable<Cat> cat = JsonNullable.undefined();
    private JsonNullable<Shelter> Shelter = JsonNullable.undefined();
    private JsonNullable<Long> thumbnailId = JsonNullable.undefined();
    private JsonNullable<List<Long>> catPictures = JsonNullable.undefined();
    private JsonNullable<String> title = JsonNullable.undefined();
    private JsonNullable<String> content = JsonNullable.undefined();

    @Getter
    public static class Cat {
        private JsonNullable<String> name = JsonNullable.undefined();
        private JsonNullable<String> age = JsonNullable.undefined();
        private JsonNullable<String> registered = JsonNullable.undefined();
        private JsonNullable<String> registNumber = JsonNullable.undefined();
        private JsonNullable<String> species = JsonNullable.undefined();
        private JsonNullable<String> sex = JsonNullable.undefined();
        private JsonNullable<Double> weight = JsonNullable.undefined();
        private JsonNullable<String> neutered = JsonNullable.undefined();
        private JsonNullable<String> healthStatus = JsonNullable.undefined();
        private JsonNullable<String> personality = JsonNullable.undefined();
        private JsonNullable<String> foundedPlace = JsonNullable.undefined();
        private JsonNullable<String> foundedAt = JsonNullable.undefined();
    }

    @Getter
    public static class Shelter {
        private JsonNullable<String> city = JsonNullable.undefined();
        private JsonNullable<String> name = JsonNullable.undefined();
        private JsonNullable<String> address = JsonNullable.undefined();
        private JsonNullable<String> phoneNumber = JsonNullable.undefined();
    }
}
