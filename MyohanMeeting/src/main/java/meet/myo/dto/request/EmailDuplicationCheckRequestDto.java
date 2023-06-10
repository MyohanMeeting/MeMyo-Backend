package meet.myo.dto.request;

import lombok.Getter;

@Getter
public class EmailDuplicationCheckRequestDto {
    private String email;

    public EmailDuplicationCheckRequestDto(String email) {
    }
}