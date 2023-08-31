package meet.myo.dto.response.adopt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import meet.myo.domain.adopt.notice.Shelter;

@Schema(name = "shelter")
@Getter
public class ShelterResponseDto {
    private String city;
    private String name;
    private String address;
    private String phoneNumber;

    public static ShelterResponseDto fromEntity(Shelter entity) {
        ShelterResponseDto schema = new ShelterResponseDto();
        schema.city = entity.getCity().toString();
        schema.name = entity.getName();
        schema.address = entity.getAddress();
        schema.phoneNumber = entity.getPhoneNumber();
        return schema;
    }
}
