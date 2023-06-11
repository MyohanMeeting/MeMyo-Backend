package meet.myo.dto.request;

import lombok.Getter;

@Getter
public class MemberOauthCreateRequestDto {
    private String oauthType;
    private String oauthId;
    private String email;
}