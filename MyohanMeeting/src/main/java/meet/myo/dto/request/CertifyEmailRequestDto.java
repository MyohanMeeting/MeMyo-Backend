package meet.myo.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class CertifyEmailRequestDto {
    private String UUID;

    public CertifyEmailRequestDto(String UUID) {
        this.UUID = UUID;
    }

}
