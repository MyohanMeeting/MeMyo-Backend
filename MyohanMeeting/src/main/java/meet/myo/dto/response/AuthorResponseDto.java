package meet.myo.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import meet.myo.domain.Member;

@Schema(name = "author")
@Getter
public class AuthorResponseDto {
    private Long authorId;
    private String nickName;
    private String profileImageUrl;

    public static AuthorResponseDto fromEntity(Member entity) {
        AuthorResponseDto author = new AuthorResponseDto();
        author.authorId = entity.getId();
        author.nickName = entity.getNickName();
        author.profileImageUrl = entity.getProfileImage().getUrl(); //TODO: QueryDsl로 최적화
        return author;
    }
}
