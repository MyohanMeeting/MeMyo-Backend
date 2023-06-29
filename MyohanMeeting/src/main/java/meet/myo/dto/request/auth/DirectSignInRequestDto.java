package meet.myo.dto.request.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Schema(name = "directSignInRequest")
@Getter
public class DirectSignInRequestDto implements SignInRequestDto {

    @Email(message = "이메일 형식이 아닙니다")
    @NotBlank(message = "이메일은 필수 입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입니다.")
//    TODO: @Size 크기 얼마나 할 지 정하기
    private String password;
}
