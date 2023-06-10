package meet.myo.dto.request;

import lombok.Getter;

@Getter
public class PasswordUpdateRequestDto {
    private String currentPassword;
    private String newPassword;
}
