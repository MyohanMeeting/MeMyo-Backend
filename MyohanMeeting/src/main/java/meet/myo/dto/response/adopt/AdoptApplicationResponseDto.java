package meet.myo.dto.response.adopt;

import lombok.Getter;
import meet.myo.dto.schema.AuthorSchema;

@Getter
public class AdoptApplicationResponseDto {

    private Long applicationId;
    private Long noticeId;
    private AuthorSchema author;
    private String content;
    private String createdAt;
    public static AdoptApplicationResponseDto fromEntity() {
        return new AdoptApplicationResponseDto();
    }
}
