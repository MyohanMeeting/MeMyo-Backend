package meet.myo.dto.request.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
public class MemberUpdateRequestDto {

    @Schema(type = "string", example = "닉네임수정")
    private JsonNullable<String> nickname = JsonNullable.undefined();

    @Schema(type = "string", example = "010-1234-5678")
    private JsonNullable<String> phoneNumber = JsonNullable.undefined();

    @Schema(type = "int", example = "1")
    private JsonNullable<Long> profileImageId = JsonNullable.undefined();
}
