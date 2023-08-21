package meet.myo.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Schema(name = "ErrorResponse")
@Getter
public class ErrorResponseDto {
    @Schema(description = "HTTP 상태 코드입니다.",
            allowableValues = {
                "200 OK", "400 BAD_REQUEST", "401 UNAUTHORIZED", "403 FORBIDDEN", "404 NOT_FOUND",
                "405 METHOD_NOT_ALLOWED", "500 INTERNAL_SERVER_ERROR"
    })
    private String status;

    @Schema(format = "date-time")
    private final String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);

    @Schema(description = "응답에 대한 요약을 나타냅니다.")
    private String message;

    @Schema(description = "오류에 관한 상세 디버깅 메시지를 나타냅니다.")
    private Object debugMessage;

    @Builder
    ErrorResponseDto(String status, String message, Object debugMessage) {
        this.status = status;
        this.message = message;
        this.debugMessage = debugMessage;
    }
}
