package meet.myo.dto.response.member;

import lombok.Getter;
import meet.myo.domain.Member;

@Getter
public class OauthRegisterResponseDto {
    private String oauthType;

    public static OauthRegisterResponseDto fromEntity(Member member) {
        OauthRegisterResponseDto dto = new OauthRegisterResponseDto();
        dto.oauthType = member.getOauth().getOauthType().toString();
        return dto;
    }
}
