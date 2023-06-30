package meet.myo.dto.response.member;

import lombok.Getter;
import meet.myo.domain.Member;

@Getter
public class MemberUpdateResponseDto {
    private String nickname;
    private String phoneNumber;
    private String profileImage;

    public static MemberUpdateResponseDto fromEntity(Member member) {
        MemberUpdateResponseDto dto = new MemberUpdateResponseDto();
        dto.nickname = member.getNickname();
        dto.phoneNumber = member.getPhoneNumber();
        dto.profileImage = member.getProfileImage().getUrl();
        return dto;
    }
}
