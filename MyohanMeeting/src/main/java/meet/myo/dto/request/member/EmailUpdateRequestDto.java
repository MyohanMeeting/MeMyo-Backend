package meet.myo.dto.request.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class EmailUpdateRequestDto {
    @Schema(type = "string", example = "newmail@test.com")
    @NotNull(message = "{validation.NotNull}")
    @Email(message = "{validation.Pattern.Email}")
    private String newEmail;
}
