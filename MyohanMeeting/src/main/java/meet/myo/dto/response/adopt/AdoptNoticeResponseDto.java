package meet.myo.dto.response.adopt;

import lombok.Getter;
import meet.myo.dto.schema.AuthorSchema;
import meet.myo.dto.schema.CatResponseSchema;
import meet.myo.dto.schema.UploadSummarySchema;

@Getter
public class AdoptNoticeResponseDto {

    private Long noticeId;

    private AuthorSchema author;

    private CatResponseSchema cat;

    private UploadSummarySchema fileSummary;

    private String status;

    private String content;

    private Integer applicationCount;

    private Integer commentCount;

    @Schema(format = "date-time")
    private String createdAt;

    public static AdoptNoticeResponseDto fromEntity() {
        AdoptNoticeResponseDto dto = new AdoptNoticeResponseDto();

        return dto;
    }
}
