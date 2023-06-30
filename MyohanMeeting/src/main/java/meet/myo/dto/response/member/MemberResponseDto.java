package meet.myo.dto.response.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import meet.myo.domain.Member;

@Schema(name = "member")
@Getter
public class MemberResponseDto {

    private String email;
    private String nickname;
    private String phoneNumber;
    private String profileImage;
    private String oauthType;

    public static MemberResponseDto fromEntity(Member member) {
        MemberResponseDto dto = new MemberResponseDto();
        dto.email = member.getEmail();
        dto.nickname = member.getNickname();
        dto.phoneNumber = member.getPhoneNumber() != null ? member.getPhoneNumber() : null;
        dto.profileImage = member.getProfileImage() != null ? member.getProfileImage().toString() : null;
        dto.oauthType = member.getOauth() != null ? member.getOauth().getOauthType().toString() : null;
        return dto;
    }
}
