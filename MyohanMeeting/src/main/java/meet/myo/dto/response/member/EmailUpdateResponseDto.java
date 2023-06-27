package meet.myo.dto.response.member;

import lombok.Getter;
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
