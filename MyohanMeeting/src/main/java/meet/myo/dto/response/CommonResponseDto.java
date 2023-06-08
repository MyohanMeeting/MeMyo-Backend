package meet.myo.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class CommonResponseDto<T> {

    private String status;
    private String message;

    private final String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);

    private T data;

    @Builder
    public CommonResponseDto(HttpStatus httpStatus, String message, T data) {
        this.status = httpStatus == null ? HttpStatus.OK.toString() : httpStatus.toString();
        this.message = message == null ? "SUCCESS" : message;
        this.data = data;
    }

}
