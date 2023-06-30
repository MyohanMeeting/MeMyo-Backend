package meet.myo.dto.request.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(name = "oauthSignInRequest")
@Getter
public class OauthSignInRequestDto implements SignInRequestDto {
    private String oauthType;
    private String oauthId;
}
