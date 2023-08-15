package meet.myo.dto.request.member;

import lombok.Getter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
public class MemberUpdateRequestDto {
    private JsonNullable<String> nickname = JsonNullable.undefined();
    private JsonNullable<String> phoneNumber = JsonNullable.undefined();
    private JsonNullable<Long> profileImageId = JsonNullable.undefined();
}
