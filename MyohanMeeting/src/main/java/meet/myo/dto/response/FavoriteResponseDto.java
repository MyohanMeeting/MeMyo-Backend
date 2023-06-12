package meet.myo.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import meet.myo.dto.schema.CatSummarySchema;

@Schema(name = "Favorite")
@Getter
public class FavoriteResponseDto {
    private Long favoriteId;
    private Long memberId;
    private CatSummarySchema cat;

    public static FavoriteResponseDto fromEntity() {
        return new FavoriteResponseDto();
    }
}
