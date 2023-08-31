package meet.myo.dto.request.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import meet.myo.util.validation.pattern.CustomPattern;
import meet.myo.util.validation.pattern.CustomPatternRegexp;

@Getter
public class PasswordUpdateRequestDto {

    @Schema(type = "string", example = "currentPassword")
    private String currentPassword;

    @Schema(type = "string", example = "newPassword")
    @Size(min = 8, max = 24, message = "{validation.Size}")
    @CustomPattern(regexp = CustomPatternRegexp.PASSWORD, message = "{validation.Pattern.password}")
    private String newPassword;
}
