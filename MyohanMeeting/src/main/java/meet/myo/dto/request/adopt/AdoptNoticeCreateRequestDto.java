package meet.myo.dto.request.adopt;

import lombok.Getter;
import meet.myo.dto.schema.CatRequestSchema;

@Getter
public class AdoptNoticeCreateRequestDto {

    private CatRequestSchema cat;
    private Long thumbnailId;
    private String title;
    private String content;
}
