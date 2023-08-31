package meet.myo.dto.response.adopt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import meet.myo.domain.adopt.notice.AdoptNotice;

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
    private Integer applicationCount;
    private Integer commentCount;

    private String createdAt;

    public static AdoptNoticeSummaryResponseDto fromEntity(AdoptNotice entity) {
        AdoptNoticeSummaryResponseDto dto = new AdoptNoticeSummaryResponseDto();
        dto.noticeId = entity.getId();
        dto.noticeTitle = entity.getTitle();
        dto.noticeStatus = entity.getNoticeStatus().toString();
        dto.thumbnail = entity.getThumbnail().toString();
        dto.authorName = entity.getMember().getNickname();
        dto.catName = entity.getCat().getName();
        dto.catSpecies = entity.getCat().getSpecies();
        dto.shelterCity = entity.getShelter().getCity().toString();
        dto.applicationCount = entity.getApplicationCount();
        dto.commentCount = entity.getCommentCount();
        dto.createdAt = entity.getCreatedAt().toString();
        return dto;
    }
}
