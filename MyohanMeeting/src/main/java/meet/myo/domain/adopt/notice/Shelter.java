package meet.myo.domain.adopt.notice;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Shelter {

    @Enumerated(EnumType.STRING)
    @Column(name="shelter_city", nullable = false)
    private City city;

    @Column(name="shelter_address", nullable = false)
    private String address;

    @Column(name="shelter_name", nullable = false)
    private String name;

    @Column(name="shelter_phoneNumber", nullable = false)
    private String phoneNumber;


    public void updateCity(City city) {
        this.city = city;
    }

    public void updateAddress(String address) {
        this.address = address;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
