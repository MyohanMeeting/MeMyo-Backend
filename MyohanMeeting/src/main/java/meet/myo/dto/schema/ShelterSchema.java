package meet.myo.dto.schema;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(name = "shelter")
@Getter
public class ShelterSchema {
    private String city;
    private String name;
    private String address;
    private String phoneNumber;
}
