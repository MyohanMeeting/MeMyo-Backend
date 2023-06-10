package meet.myo.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import meet.myo.domain.Member;

@Getter
@NoArgsConstructor
public class PasswordUpdateResponseDto {

    public static PasswordUpdateResponseDto fromEntity(Member member) {

        return new PasswordUpdateResponseDto();
    }
}
