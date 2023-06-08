package meet.myo.domain.adopt.notice;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Shelter {

    @Column(name="shelter_city", nullable = false)
    private City city;

    @Column(name="shelter_address", nullable = false)
    private String address;

    @Column(name="shelter_name", nullable = false)
    private String name;

    @Column(name="shelter_phoneNumber", nullable = false)
    private String phoneNumber;

}
