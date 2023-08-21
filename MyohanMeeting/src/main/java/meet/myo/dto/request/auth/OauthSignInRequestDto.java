package meet.myo.dto.request.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import meet.myo.domain.OauthType;
import meet.myo.util.validation.enums.EnumValid;

@Schema(name = "oauthSignInRequest")
@Getter
public class OauthSignInRequestDto implements SignInRequestDto {

    @Schema(type = "string", allowableValues = {"KAKAOTALK"}, example = "KAKAOTALK")
    @NotNull(message = "{validation.NotNull}")
    @EnumValid(enumClass = OauthType.class)
    private String oauthType;

    @Schema(type = "string", example = "kakaotest")
    @NotNull(message = "{validation.NotNull}")
    private String oauthId;
}
