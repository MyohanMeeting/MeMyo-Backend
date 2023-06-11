package meet.myo.dto.response;

import meet.myo.domain.Member;

public class MemberResponseDto {

    private String name;
    private String email;
    private String nickName;
    private String phoneNumber;

    public static MemberResponseDto fromEntity(Member member) {
        MemberResponseDto dto = new MemberResponseDto();
        dto.name = member.getName();
        dto.email = member.getEmail();
        dto.nickName = member.getNickName();
        dto.phoneNumber = member.getPhoneNumber();
        return dto;
    }
}

