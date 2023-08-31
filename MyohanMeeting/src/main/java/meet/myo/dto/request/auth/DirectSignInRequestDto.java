package meet.myo.dto.request.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import meet.myo.util.validation.pattern.CustomPattern;
import meet.myo.util.validation.pattern.CustomPatternRegexp;

@Schema(name = "directSignInRequest")
@Getter
public class DirectSignInRequestDto implements SignInRequestDto {

    @Schema(type = "string", example = "user@user.com")
    @NotNull(message = "{validation.NotNull}")
    @Email(message = "{validation.Pattern.email}")
    private String email;

    @Schema(type = "string", example = "user1234!")
    @NotNull(message = "{validation.NotNull}")
    @Size(min = 8, max = 24, message = "{validation.Size}")
    @CustomPattern(regexp = CustomPatternRegexp.PASSWORD, message = "{validation.Pattern.password}")
    private String password;
}
