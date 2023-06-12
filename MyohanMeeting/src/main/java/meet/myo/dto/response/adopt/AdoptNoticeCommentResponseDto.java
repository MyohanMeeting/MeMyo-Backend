package meet.myo.dto.response.adopt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import meet.myo.dto.schema.AuthorSchema;

@Schema(name = "AdoptNoticeComment")
@Getter
public class AdoptNoticeCommentResponseDto {

    private Long noticeCommentId;
    private Long noticeId;
    private AuthorSchema author;
    private String content;
    private String createdAt;

    //TODO: QueryDsl로 최적화
    public static AdoptNoticeCommentResponseDto fromEntity() {

        return new AdoptNoticeCommentResponseDto();
    }
//    public static AdoptNoticeCommentResponseDto fromEntity(AdoptNoticeComment comment, Member member) {
//
//        AdoptNoticeCommentResponseDto dto = new AdoptNoticeCommentResponseDto();
//
//        dto.noticeCommentId = comment.getId();
//        dto.noticeId = comment.getAdoptNotice().getId();
//        dto.author = AuthorSchema.fromEntity(member);
//        dto.content = comment.getContent();
//        dto.createdAt = comment.getCreatedAt().toString();
//
//        return dto;
//    }
}
