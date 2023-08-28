package meet.myo.dto.request.adopt;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import meet.myo.domain.adopt.notice.City;
import meet.myo.domain.adopt.notice.cat.Neutered;
import meet.myo.domain.adopt.notice.cat.Registered;
import meet.myo.domain.adopt.notice.cat.Sex;
import meet.myo.util.validation.enums.EnumValid;
import meet.myo.util.validation.pattern.CustomPattern;
import meet.myo.util.validation.pattern.CustomPatternRegexp;

import java.util.List;


@Schema(name = "adoptNoticeForm")
@Getter
public class AdoptNoticeCreateRequestDto {

    @Valid
    @NotNull(message = "{validation.NotNull}")
    private Cat cat;

    @Valid
    @NotNull(message = "{validation.NotNull}")
    private Shelter shelter;

    @Schema(type = "int", example = "2")
    @NotNull(message = "{validation.NotNull}")
    private Long thumbnailId;

    @Schema(type = "array", example = "[2, 3, 4, 5, 6]")
    @NotNull(message = "{validation.NotNull}")
    private List<Long> catPictures;

    @Schema(type = "string", example = "고등어 코숏 데려가세요")
    @NotNull(message = "{validation.NotNull}")
    @Size(max = 255, message = "{validation.Size}")
    private String title;

    @Schema(type = "string", example = "애교 많고 귀여운 아이입니다.")
    @NotNull(message = "{validation.NotNull}")
    @Size(max = 5000, message = "{validation.Size}")
    private String content;

    @Getter
    public static class Cat {

        @Schema(type = "string", example = "미미")
        @NotNull(message = "{validation.NotNull}")
        @Size(max = 32, message = "{validation.Size}")
        private String name;

        @Schema(type = "string", example = "1세 추정")
        @NotNull(message = "{validation.NotNull}")
        @Size(max = 32, message = "{validation.Size}")
        private String age;

        @Schema(type = "string", description = "농림축산검역본부 국가동물정보보호시스템에 구조동물로 등록되었는지 여부를 입력합니다.",
                allowableValues = {"REGISTERED", "NOT_REGISTERED"},
                example = "REGISTERED")
        @NotNull(message = "{validation.NotNull}")
        @EnumValid(enumClass = Registered.class)
        private String registered;

        @Schema(type = "string", description = "농림축산검역본부 국가동물정보보호시스템에 등록된 구조동물 관리번호를 입력합니다.",
                example = "전북-군산-2023-00000")
        @Size(max = 32, message = "{validation.Size}")
        private String registNumber;

        @Schema(type = "string", example = "코숏 추정")
        @NotNull(message = "{validation.NotNull}")
        @Size(max = 255, message = "{validation.Size}")
        private String species;

        @Schema(type = "float", description = "몸무게(kg)", example = "4.5")
        @NotNull(message = "{validation.NotNull}")
        private Double weight;

        @Schema(type = "string", allowableValues = {"MALE", "FEMALE", "OTHER"}, example = "FEMALE")
        @NotNull(message = "{validation.NotNull}")
        @EnumValid(enumClass = Sex.class)
        private String sex;

        @Schema(type = "string", allowableValues = {"NEUTERED", "NOT_NEUTERED", "UNKNOWN"}, example = "NEUTERED")
        @NotNull(message = "{validation.NotNull}")
        @EnumValid(enumClass = Neutered.class)
        private String neutered;

        @Schema(type = "string", example = "건강에 아무 이상 없고 예방접종도 모두 마쳤어요.")
        @NotNull(message = "{validation.NotNull}")
        @Size(max = 2000, message = "{validation.Size}")
        private String healthStatus;

        @Schema(type = "string", example = "처음엔 조금 낯을 가리지만 익숙해지면 애교가 많아요. 활달한 편이에요.")
        @NotNull(message = "{validation.NotNull}")
        @Size(max = 3000, message = "{validation.Size}")
        private String personality;

        @Schema(type = "string", example = "전북 군산시 나운동")
        @NotNull(message = "{validation.NotNull}")
        @Size(max = 255, message = "{validation.Size}")
        private String foundedPlace;

        @Schema(type = "string", example = "2023년 7월 추정")
        @NotNull(message = "{validation.NotNull}")
        @Size(max = 255, message = "{validation.Size}")
        private String foundedAt;
    }

    @Getter
    public static class Shelter {

        @Schema(type = "string", allowableValues = {"SEOUL", "SEJONG", "BUSAN", "DAEGU", "INCHEON", "GWANGJU",
                "ULSAN", "DAEJEON", "GYEONGGI", "GANGWON", "CHUNGCHEONG_BUK", "CHUNGCHEONG_NAM", "JEOLLA_BUK",
                "JEOLLA_NAM", "GYEONGSANG_BUK", "GYEONGSANG_NAM", "JEJU"}, example = "JEOLLA_BUK")
        @NotNull(message = "{validation.NotNull}")
        @EnumValid(enumClass = City.class)
        private String city;

        @Schema(type = "string", example = "묘한보호소")
        @NotNull(message = "{validation.NotNull}")
        @Size(max = 255, message = "{validation.Size}")
        private String name;

        @Schema(type = "string", example = "전북 군산시 묘한동 만남로 123")
        @NotNull(message = "{validation.NotNull}")
        @Size(max = 255, message = "{validation.Size}")
        private String address;

        @Schema(type = "string", example = "010-1234-1234")
        @NotNull(message = "{validation.NotNull}")
        @CustomPattern(regexp = CustomPatternRegexp.PHONE_NUMBER, message = "{validation.Pattern.phoneNumber}")
        private String phoneNumber;
    }
}
