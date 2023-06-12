package meet.myo.dto.response.adopt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import meet.myo.dto.schema.AuthorSchema;

@Schema(name = "AdoptApplication")
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
