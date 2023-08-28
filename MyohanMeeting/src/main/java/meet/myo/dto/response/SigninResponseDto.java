package meet.myo.dto.response;

import lombok.Builder;
import lombok.Getter;
import meet.myo.jwt.TokenDto;

@Getter
public class SignInResponseDto {
    private String email;
    private String nickName;
    private String profileImageUrl;
    private String accessToken;
    private String refreshToken;

    @Builder
    public SignInResponseDto(String email, String nickName, String profileImageUrl, TokenDto token) {
        this.email = email;
        this.nickName = nickName;
        this.profileImageUrl = profileImageUrl;
        this.accessToken = token.getAccessToken();
        this.refreshToken = token.getRefreshToken();
    }
}
