package meet.myo.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OauthUpdateRequestDto {
    private String oauthType;
    private String oauthId;
}