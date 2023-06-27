package meet.myo.dto.response.member;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;

import java.util.List;

@Hidden
@Getter
public class MemberAuthorityResponseDto {

    private Long memberId;
    private List<String> authorities;

    public static MemberAuthorityResponseDto fromEntity() {
        return new MemberAuthorityResponseDto();
    }
}
