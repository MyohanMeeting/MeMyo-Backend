package meet.myo.dto.request;

import lombok.Getter;

@Getter
public class OauthSignInRequestDto implements SignInRequestDto {
    private String oauthType;
    private String oauthId;
}
