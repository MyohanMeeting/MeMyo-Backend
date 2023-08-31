package meet.myo.dto.request.member;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OauthDeleteRequestDto {
    @NotNull(message = "{validation.NotNull}")
    private String password;
}
