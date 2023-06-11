package meet.myo.dto.request.adopt;

import lombok.Getter;

@Getter
public class AdoptNoticeCommentCreateRequestDto {
    private Long noticeId;
    private String content;
}
