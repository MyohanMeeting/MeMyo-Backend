package meet.myo.dto.request.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import meet.myo.domain.OauthType;
import meet.myo.util.validation.enums.EnumValid;

@Getter
public class OauthUpdateRequestDto {

    @Schema(type = "string", allowableValues = {"KAKAOTALK"}, example = "KAKAOTALK")
    @NotNull(message = "{validation.NotNull}")
    @EnumValid(enumClass = OauthType.class)
    private String oauthType;

    @NotNull(message = "{validation.NotNull}")
    private String oauthId;
}
