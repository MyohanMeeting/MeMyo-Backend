package meet.myo.dto.request.adopt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;


@Schema(name = "adoptNoticeForm")
@Getter
public class AdoptNoticeCreateRequestDto {

    private Cat cat;
    private Shelter shelter;
    private Long thumbnailId;
    private List<Long> catPictures;
    private String title;
    private String content;

    @Getter
    public static class Cat {

        private String name;
        private String age;
        private String registered;
        private String registNumber;
        private String species;
        private String sex;
        private Double weight;
        private String neutered;
        private String healthStatus;
        private String personality;
        private String foundedPlace;
        private String foundedAt;
    }

    @Getter
    public static class Shelter {
        private String city;
        private String name;
        private String address;
        private String phoneNumber;
    }
}
