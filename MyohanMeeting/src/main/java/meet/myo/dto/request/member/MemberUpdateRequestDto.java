package meet.myo.dto.request.member;

import lombok.Getter;

@Getter
public class MemberUpdateRequestDto {
    private String name;
    private String nickName;
    private String phoneNumber;
    private String profileImage;
}
