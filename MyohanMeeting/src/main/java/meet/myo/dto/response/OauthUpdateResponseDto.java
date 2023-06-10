package meet.myo.dto.response;

import lombok.Getter;
import meet.myo.domain.Member;
import meet.myo.domain.OauthType;

@Getter
public class OauthUpdateResponseDto {
    private OauthType oauthType;
    private String oauthId;

    public OauthUpdateResponseDto(OauthType oauthType, String oauthId) {
    }

    public static OauthUpdateResponseDto fromEntity(Member member) {
        return new OauthUpdateResponseDto(member.getOauth().getOauthType(), member.getOauth().getOauthId());
    }
}
