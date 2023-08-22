package meet.myo.dto.request.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import meet.myo.util.validation.pattern.CustomPattern;
import meet.myo.util.validation.pattern.CustomPatternRegexp;

@Getter
public class MemberDirectCreateRequestDto {

    @Schema(type = "string", example = "jointest@test.com")
    @Email(message = "{validation.Pattern.email}")
    @NotNull(message="{validation.NotNull}")
    private String email;

    @Schema(type = "string", example = "user1234!")
    @NotNull(message="{validation.NotNull}")
    @Size(min = 8, max = 24, message = "{validation.Size}")
    @CustomPattern(regexp = CustomPatternRegexp.PASSWORD, message = "{validation.Pattern.password}")
    private String password;

    @NotNull(message = "{validation.NotNull}")
    @Size(min = 2, max = 12, message = "{validation.Size}")
    private String nickname;

    @NotNull(message = "{validation.NotNull}")
    @CustomPattern(regexp = CustomPatternRegexp.PHONE_NUMBER, message = "{validation.Pattern.phoneNumber}")
    private String phoneNumber;
}
