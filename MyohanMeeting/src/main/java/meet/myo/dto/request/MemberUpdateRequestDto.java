package meet.myo.dto.request;

import lombok.Getter;

@Getter
public class MemberUpdateRequestDto {
    private String name;
    private String nickName;
    private String phoneNumber;
}