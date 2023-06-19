package meet.myo.dto.request;

import lombok.Getter;
import meet.myo.domain.Member;
import meet.myo.domain.OauthType;

@Getter
public class MemberOauthCreateRequestDto {
    private String oauthType;
    private String oauthId;
    private String email;

    public Member toEntity() {
        return Member.oauthJoinBuilder()
                .email(this.email)
                .oauthType(OauthType.valueOf(this.oauthType))
                .oauthId(this.oauthId)
                .build();
    }
}