package meet.myo.dto.response.adopt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import meet.myo.domain.adopt.notice.AdoptNoticeComment;
import meet.myo.dto.response.AuthorResponseDto;

import java.time.LocalDateTime;

@Schema(name = "AdoptNoticeComment")
@Getter
public class AdoptNoticeCommentResponseDto {

    private Long noticeCommentId;
    private Long noticeId;
    private AuthorResponseDto author;
    private String content;
    private LocalDateTime createdAt;

    //TODO: QueryDsl로 최적화
    public static AdoptNoticeCommentResponseDto fromEntity(AdoptNoticeComment comment) {

        AdoptNoticeCommentResponseDto dto = new AdoptNoticeCommentResponseDto();

        dto.noticeCommentId = comment.getId();
        dto.noticeId = comment.getAdoptNotice().getId();
        dto.author = AuthorResponseDto.fromEntity(comment.getMember());
        dto.content = comment.getContent();
        dto.createdAt = comment.getCreatedAt();

        return dto;
    }
}
