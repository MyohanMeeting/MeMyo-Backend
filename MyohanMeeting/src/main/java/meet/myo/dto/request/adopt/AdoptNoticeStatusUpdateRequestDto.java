package meet.myo.dto.request.adopt;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class AdoptNoticeStatusUpdateRequestDto {
    private String status;
}
