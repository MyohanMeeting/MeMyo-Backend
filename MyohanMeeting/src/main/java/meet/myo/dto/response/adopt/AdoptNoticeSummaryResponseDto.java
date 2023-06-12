package meet.myo.dto.response.adopt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(name = "AdoptNoticeSummary")
@Getter
public class AdoptNoticeSummaryResponseDto {
    private Long noticeId;
    private String noticeTitle;
    private String noticeStatus;
    private String thumbnail;
    private String authorName;
    private String catName;
    private String catSpecies;
    private String shelterCity; //    SEOUL, SEJONG, BUSAN, DAEGU, INCHEON, GWANGJU, ULSAN, DAEJEON,
                                // GYEONGGI, GANGWON, CHUNGCHEONG_BUK, CHUNGCHEONG_NAM, JEOLLA_BUK, JEOLLA_NAM,
                                // GYEONGSANG_BUK, GYEONGSANG_NAM, JEJU


    public static AdoptNoticeSummaryResponseDto fromEntity() {
        return new AdoptNoticeSummaryResponseDto();
    }
}
