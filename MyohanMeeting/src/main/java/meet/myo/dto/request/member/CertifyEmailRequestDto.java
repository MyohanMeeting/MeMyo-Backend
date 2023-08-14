package meet.myo.dto.request.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CertifyEmailRequestDto {
    @JsonProperty("UUID")
    private String UUID;
}