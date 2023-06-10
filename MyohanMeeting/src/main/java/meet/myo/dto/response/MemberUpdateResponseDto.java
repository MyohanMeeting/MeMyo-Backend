package meet.myo.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import meet.myo.domain.Member;

@RequiredArgsConstructor
@Getter
@Setter
public class MemberUpdateResponseDto {
    private String name;
    private String nickName;
    private String phoneNumber;

    public static MemberUpdateResponseDto fromEntity(Member member) {
        MemberUpdateResponseDto dto = new MemberUpdateResponseDto();
        dto.setName(member.getName());
        dto.setNickName(member.getNickName());
        dto.setPhoneNumber(member.getPhoneNumber());
        return dto;
    }
}