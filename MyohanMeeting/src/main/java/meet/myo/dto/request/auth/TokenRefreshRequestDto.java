package meet.myo.dto.request.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(name = "tokenRefreshRequest")
@Getter
public class TokenRefreshRequestDto {
    private String refreshToken;
}
