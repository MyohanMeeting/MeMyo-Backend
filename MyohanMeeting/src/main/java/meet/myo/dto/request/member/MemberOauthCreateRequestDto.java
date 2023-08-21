package meet.myo.dto.request.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import meet.myo.domain.OauthType;
import meet.myo.util.validation.enums.EnumValid;

@Getter
public class MemberOauthCreateRequestDto {

    @Schema(type = "string", allowableValues = {"KAKAOTALK"}, example = "KAKAOTALK")
    @NotNull(message = "{validation.NotNull}")
    @EnumValid(enumClass = OauthType.class)
    private String oauthType;

    @Schema(type = "string", example = "kakaotest")
    @NotNull(message = "{validation.NotNull}")
    private String oauthId;

    @Schema(type = "string", example = "kakaotest@kakao.com")
    @NotNull(message = "{validation.NotNull}")
    @Email(message = "{validation.Pattern.email}")
    private String email;

//    @NotNull(message = "{validation.NotNull}")
    @Size(min = 2, max = 12, message = "{validation.ValidJsonNullable}")
    private String nickname;
}
