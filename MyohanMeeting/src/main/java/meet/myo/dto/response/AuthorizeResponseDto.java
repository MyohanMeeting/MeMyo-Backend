package meet.myo.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthorizeResponseDto {
    private String accessToken;
    private String refreshToken;

    @Builder
    public AuthorizeResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}