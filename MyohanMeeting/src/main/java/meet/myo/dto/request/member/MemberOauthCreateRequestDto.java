package meet.myo.dto.request.member;

import lombok.Getter;

@Getter
public class MemberOauthCreateRequestDto {
    private String oauthType;
    private String oauthId;
    private String email;
    private String nickname;
}
