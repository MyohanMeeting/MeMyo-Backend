package meet.myo.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import meet.myo.domain.Member;

@Getter
public class EmailUpdateResponseDto {
    private String newEmail;

    public static EmailUpdateResponseDto fromEntity(Member member) {
        EmailUpdateResponseDto dto = new EmailUpdateResponseDto();
        dto.newEmail = member.getEmail();
        return dto;
    }
}