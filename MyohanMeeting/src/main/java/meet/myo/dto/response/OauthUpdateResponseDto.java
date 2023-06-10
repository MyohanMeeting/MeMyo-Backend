package meet.myo.dto.response;

import lombok.Getter;
import meet.myo.domain.Member;
import meet.myo.domain.OauthType;
import meet.myo.dto.request.OauthUpdateRequestDto;

@Getter
public class OauthUpdateResponseDto {
    private String oauthType;
    private String oauthId;

    // 객체 생성 패턴 : 빌더/스태틱 메서드
    public static OauthUpdateResponseDto fromEntity(Member member) {
        OauthUpdateResponseDto dto = new OauthUpdateResponseDto();
        dto.oauthType = member.getOauth().getOauthType().toString();
        dto.oauthId = member.getOauth().getOauthId();
        return dto;
    }
}
