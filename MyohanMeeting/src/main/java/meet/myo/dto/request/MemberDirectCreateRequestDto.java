package meet.myo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import meet.myo.domain.Member;

@Getter
public class MemberDirectCreateRequestDto {

    @Email(message = "INVALID_EMAIL")
    @NotBlank(message="EMAIL_IS_MANDATORY")
    private String email;

    private String name;

    @NotBlank(message = "PASSWORD_IS_MANDATORY")
    private String password;

    private String nickName;

    private String phoneNumber;

    public Member toEntity(String encodedPassword) {
        return Member.directJoinBuilder()
                .email(this.email)
                .name(this.name)
                .nickName(this.nickName)
                .phoneNumber(this.phoneNumber)
                .build();
    }
}