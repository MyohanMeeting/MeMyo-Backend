package meet.myo.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class CommonResponseDto<T> {

    @Schema(example = "200 OK")
    private String status;

    @Schema(format = "date-time")
    private final String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);

    @Schema(example = "SUCCESS")
    private String message;

    private T data;

    @Builder
    public CommonResponseDto(HttpStatus httpStatus, String message, T data) {
        this.status = httpStatus == null ? HttpStatus.OK.toString() : httpStatus.toString();
        this.message = message == null ? "SUCCESS" : message;
        this.data = data;
    }

}
