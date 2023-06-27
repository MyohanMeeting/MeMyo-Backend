package meet.myo.dto.response.adopt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import meet.myo.domain.adopt.notice.AdoptNotice;
import meet.myo.dto.response.AuthorResponseDto;
import meet.myo.dto.response.UploadSummaryResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Schema(name = "AdoptNoticeDetail")
public class AdoptNoticeResponseDto {

    private Long noticeId;

    private AuthorResponseDto author;

    private CatResponseDto cat;

    private ShelterResponseDto shelter;

    private UploadSummaryResponseDto thumbnail;

    private List<UploadSummaryResponseDto> catPictures;

    private String status;

    private String content;

    private Integer applicationCount;

    private Integer commentCount;

    @Schema(format = "date-time")
    private LocalDateTime createdAt;

    public static AdoptNoticeResponseDto fromEntity(AdoptNotice entity) {
        AdoptNoticeResponseDto dto = new AdoptNoticeResponseDto();
        dto.noticeId = entity.getId();
        dto.author = AuthorResponseDto.fromEntity(entity.getMember());
        dto.cat = CatResponseDto.fromEntity(entity.getCat());
        dto.shelter = ShelterResponseDto.fromEntity(entity.getShelter());
        dto.thumbnail = UploadSummaryResponseDto.fromEntity(entity.getThumbnail());
        dto.catPictures = entity.getCatPictures().stream().map(p -> UploadSummaryResponseDto.fromEntity(p.getUpload())).toList();
        dto.status = entity.getNoticeStatus().toString();
        dto.content = entity.getContent();
        dto.applicationCount = entity.getApplicationCount();
        dto.commentCount = entity.getCommentCount();
        dto.createdAt = entity.getCreatedAt();
        return dto;
    }
}
