package meet.myo.dto.schema;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(name = "CatSummary")
@Getter
public class CatSummarySchema {
    private Long catId;
    private String catName;
    private String catSpecies;
    private String catPicture;
    private Long noticeId;
    private String shelterCity; //    SEOUL, SEJONG, BUSAN, DAEGU, INCHEON, GWANGJU, ULSAN, DAEJEON,
                                // GYEONGGI, GANGWON, CHUNGCHEONG_BUK, CHUNGCHEONG_NAM, JEOLLA_BUK, JEOLLA_NAM,
                                // GYEONGSANG_BUK, GYEONGSANG_NAM, JEJU
}
