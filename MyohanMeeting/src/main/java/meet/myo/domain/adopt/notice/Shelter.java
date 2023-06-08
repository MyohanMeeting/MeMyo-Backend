package meet.myo.domain.adopt.notice;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Shelter {

    @Column(name="shelter_city", nullable = false)
    private City city;

    @Column(name="shelter_address", nullable = false)
    private String address;

    @Column(name="shelter_name", nullable = false)
    private String name;

    @Column(name="shelter_phoneNumber", nullable = false)
    private String phoneNumber;

    private Shelter(String city, String address, String name, String phoneNumber) {
        this.city = city;
        this.address = address;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public static Shelter createShelter(String city, String address, String name, String phoneNumber) {
        return new Shelter(city, address, name, phoneNumber);
    }
}
