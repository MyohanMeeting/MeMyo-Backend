package meet.myo.dto.response.member;

import lombok.Getter;
import meet.myo.domain.Member;

@Getter
public class OauthUpdateResponseDto {
    private String oauthType;

    public static OauthUpdateResponseDto fromEntity(Member member) {
        OauthUpdateResponseDto dto = new OauthUpdateResponseDto();
        dto.oauthType = member.getOauth().getOauthType().toString();
        return dto;
    }
}
