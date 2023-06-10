package meet.myo.dto.response;

import lombok.Builder;
import meet.myo.domain.Member;

@Builder
public class MemberResponseDto {
    private String name;
    private String email;
    private String nickName;
    private String phoneNumber;
    public static MemberResponseDto fromEntity(Member member) {
        return MemberResponseDto.builder()
                .name(member.getName())
                .email(member.getEmail())
                .nickName(member.getNickName())
                .phoneNumber(member.getPhoneNumber())
                .build();
    }
}

