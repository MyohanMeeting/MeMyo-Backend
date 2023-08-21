package meet.myo.dto.request.adopt;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import meet.myo.domain.adopt.application.Gender;
import meet.myo.domain.adopt.application.Married;
import meet.myo.domain.adopt.application.YesOrNo;
import meet.myo.util.validation.enums.EnumValid;
import meet.myo.util.validation.pattern.CustomPattern;
import meet.myo.util.validation.pattern.CustomPatternRegexp;

@Schema(name = "adoptApplicationForm")
@Getter
public class AdoptApplicationCreateRequestDto {


    @Schema(type = "int", example = "1")
    @NotNull(message = "{validation.NotNull}")
    private Long noticeId;

    @NotNull(message = "{validation.NotNull}")
    private Applicant applicant;

    @NotNull(message = "{validation.NotNull}")
    private Survey survey;

    @Schema(type = "string", example = "입양 신청합니다.")
    @NotNull(message = "{validation.NotNull}")
    @Size(max = 5000, message = "{validation.Size}")
    private String content;

    @Getter
    public static class Applicant {

        @Schema(type = "string", example = "김신청")
        @NotNull(message = "{validation.NotNull}")
        @Size(min = 2, max = 32, message = "{validation.Size}")
        private String name;

        @Schema(type = "int", example = "28")
        @NotNull(message = "{validation.NotNull}")
        private Integer age;

        @Schema(type = "string", allowableValues = {"MALE", "FEMALE", "OTHER"}, example = "FEMALE")
        @NotNull(message = "{validation.NotNull}")
        @EnumValid(enumClass = Gender.class)
        private String gender;

        @Schema(type = "string", example = "서울시 중구 필동 99가 9동 9호")
        @NotNull(message = "{validation.NotNull}")
        @Size(max = 255, message = "{validation.Size}")
        private String address;

        @Schema(type = "string", example = "010-123-1234")
        @CustomPattern(regexp = CustomPatternRegexp.PHONE_NUMBER, message = "{validation.Pattern.phoneNumber}")
        private String phoneNumber;

        @Schema(type = "string", example = "회사원")
        @NotNull(message = "{validation.NotNull}")
        @Size(max = 255, message = "{validation.Size}")
        private String job;

        @Schema(type = "string", allowableValues = {"MARRIED", "UNMARRIED"}, example = "UNMARRIED")
        @NotNull(message = "{validation.NotNull}")
        @EnumValid(enumClass = Married.class)
        private String married;

    }
    @Getter
    public static class Survey {
        @Schema(type = "string", allowableValues = {"YES", "NO"},
                description = "1-1. 반려동물을 키우신 적이 있습니까?", example = "YES")
        @NotNull(message = "{validation.NotNull}")
        @EnumValid(enumClass = YesOrNo.class)
        private String answer1_1;

        @Schema(type = "string", description = "1-2. 어떤 종류의 동물인지, 얼마나 키우셨는지, 지금은 어떻게 되었는지 적어주세요.",
                example = "고양이를 키우고 있습니다. 이름은 레아입니다.")
        @Size(max = 3000, message = "{validation.Size}")
        private String answer1_2;

        @Schema(type = "string", allowableValues = {"YES", "NO"},
                description = "2-1. 현재 집에 다른 동물을 키우고 계십니까?", example = "YES")
        @NotNull(message = "{validation.NotNull}")
        @EnumValid(enumClass = YesOrNo.class)
        private String answer2_1;

        @Schema(type = "string", description = "2-2. 동물의 종류와 나이, 성별, 중성화 여부를 적어주세요.",
                example = "13살 코숏이고 겁도 애교도 많아요. 중성화한 남아입니다.")
        @Size(max = 3000, message = "{validation.Size}")
        private String answer2_2;

        @Schema(type = "string",  description = "3. 가족 구성원은 어떻게 되시나요?",
                example = "저 혼자 살고 있습니다.")
        @NotNull(message = "{validation.NotNull}")
        @Size(max = 1000, message = "{validation.Size}")
        private String answer3;

        @Schema(type = "string", allowableValues = {"YES", "NO", "PARTIAL"},
                description = "4. 가족들은 유기동물 입양을 찬성하나요?", example = "YES")
        @NotNull(message = "{validation.NotNull}")
        @EnumValid(enumClass = YesOrNo.class)
        private String answer4;

        @Schema(type = "string", description = "5. 입양을 원하는 이유는 무엇인가요?",
                example = "미미의 사진을 보는 순간 사랑에 빠졌어요. 안타까운 사연을 지닌 미미의 상처를 치료해주는 좋은 보호자가 되고 싶습니다.")
        @NotNull(message = "{validation.NotNull}")
        @Size(max = 3000, message = "{validation.Size}")
        private String answer5;

        @Schema(type = "string", allowableValues = {"YES", "NO"},
                description = "6. 만일 분양자가 원한다면 입양 후 입양동물의 사진을 전해주실 수 있나요?", example = "YES")
        @NotNull(message = "{validation.NotNull}")
        @EnumValid(enumClass = YesOrNo.class)
        private String answer6;
    }
}


