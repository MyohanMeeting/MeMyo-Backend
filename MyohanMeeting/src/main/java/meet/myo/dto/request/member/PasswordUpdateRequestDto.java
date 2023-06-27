package meet.myo.dto.request.member;

import lombok.Getter;

@Getter
public class PasswordUpdateRequestDto {
    private String currentPassword;
    private String newPassword;
}
