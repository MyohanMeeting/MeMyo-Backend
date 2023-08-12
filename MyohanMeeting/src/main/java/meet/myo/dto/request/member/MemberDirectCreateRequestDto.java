package meet.myo.dto.request.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MemberDirectCreateRequestDto {

    @Email(message = "INVALID_EMAIL")
    @NotBlank(message="EMAIL_IS_MANDATORY")
    private String email;
    @NotBlank(message = "PASSWORD_IS_MANDATORY")
    private String password;
    private String nickname;
    private String phoneNumber;
    private Long profileImageId;
}
