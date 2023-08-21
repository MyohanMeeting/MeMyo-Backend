package meet.myo.dto.request.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Schema(name = "tokenRefreshRequest")
@Getter
public class TokenRefreshRequestDto {
    @NotNull(message = "{validation.NotNull}")
    private String refreshToken;
}
