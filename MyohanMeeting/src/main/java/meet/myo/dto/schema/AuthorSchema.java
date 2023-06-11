package meet.myo.dto.schema;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import meet.myo.domain.Member;

@Schema(name = "author")
@Getter
public class AuthorSchema {
    private Long authorId;
    private String nickName;
    private String profileImageUrl;

    public static AuthorSchema fromEntity(Member entity) {
        AuthorSchema author = new AuthorSchema();
        author.authorId = entity.getId();
        author.nickName = entity.getNickName();
        author.profileImageUrl = entity.getProfileImage().getUrl(); //TODO: QueryDsl로 최적화
        return author;
    }
}