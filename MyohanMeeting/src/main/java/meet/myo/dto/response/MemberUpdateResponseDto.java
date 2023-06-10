package meet.myo.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import meet.myo.domain.Member;

@Getter
public class MemberUpdateResponseDto {
    private String name;
    private String nickName;
    private String phoneNumber;

    public static MemberUpdateResponseDto fromEntity(Member member) {
        MemberUpdateResponseDto dto = new MemberUpdateResponseDto();
        dto.name = member.getName();
        dto.nickName = member.getNickName();
        dto.phoneNumber = member.getPhoneNumber();
        return dto;
    }
}