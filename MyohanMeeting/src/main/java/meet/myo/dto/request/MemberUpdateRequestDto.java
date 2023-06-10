package meet.myo.dto.request;

import lombok.Getter;

@Getter
public class MemberUpdateRequestDto {
    private Long memberId;
    private String name;
    private String nickName;
    private String phoneNumber;
}