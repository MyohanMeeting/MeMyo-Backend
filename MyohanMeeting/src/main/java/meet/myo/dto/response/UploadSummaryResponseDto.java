package meet.myo.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import meet.myo.domain.Upload;

@Schema(name = "uploadSummary")
@Getter
public class UploadSummaryResponseDto {
    private Long uploadId;
    private String url;

    public static UploadSummaryResponseDto fromEntity(Upload entity) {
        UploadSummaryResponseDto schema = new UploadSummaryResponseDto();
        schema.uploadId = entity.getId();
        schema.url = entity.getUrl();
        return schema;
    }
}
