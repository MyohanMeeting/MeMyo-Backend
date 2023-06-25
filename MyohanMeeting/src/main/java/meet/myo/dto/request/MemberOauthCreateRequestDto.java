package meet.myo.dto.request;

import lombok.Getter;
import meet.myo.domain.Member;
import meet.myo.domain.OauthType;

@Getter
public class MemberOauthCreateRequestDto {
    private String oauthType;
    private String oauthId;
    private String email;
    private String nickName;
}