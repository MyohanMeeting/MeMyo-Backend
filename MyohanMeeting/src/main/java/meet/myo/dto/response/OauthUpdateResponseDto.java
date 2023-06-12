package meet.myo.dto.response;

import lombok.Getter;
import meet.myo.domain.Member;
import meet.myo.domain.OauthType;
import meet.myo.dto.request.OauthUpdateRequestDto;

@Getter
public class OauthUpdateResponseDto {
    private String oauthType;

    public static OauthUpdateResponseDto fromEntity(Member member) {
        OauthUpdateResponseDto dto = new OauthUpdateResponseDto();
        dto.oauthType = member.getOauth().getOauthType().toString();
        return dto;
    }
}
