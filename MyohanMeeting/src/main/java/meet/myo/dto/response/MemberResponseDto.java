package meet.myo.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import meet.myo.domain.Member;

@Schema(name = "member")
@Getter
public class MemberResponseDto {

    private String email;
    private String nickName;
    private String phoneNumber;
    private String profileImage;
    private String oauthType;

    public static MemberResponseDto fromEntity(Member member) {
        MemberResponseDto dto = new MemberResponseDto();
        dto.email = member.getEmail();
        dto.nickName = member.getNickName();
        dto.phoneNumber = member.getPhoneNumber();
        dto.profileImage = member.getProfileImage().getUrl();
        dto.oauthType = member.getOauth().getOauthType().toString();
        return dto;
    }
}

