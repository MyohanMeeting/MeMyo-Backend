package meet.myo.dto.schema;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(name = "uploadSummary")
@Getter
public class UploadSummarySchema {
    private Long uploadId;
    private String url;
}
