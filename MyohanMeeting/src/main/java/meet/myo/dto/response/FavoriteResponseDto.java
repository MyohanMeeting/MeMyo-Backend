package meet.myo.dto.response;

import meet.myo.domain.Favorite;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;


@Schema(name = "Favorite")
@Getter
public class FavoriteResponseDto {
    private Long favoriteId;
    private Long memberId;



    public static FavoriteResponseDto fromEntity(Favorite favorite) {
        FavoriteResponseDto dto = new FavoriteResponseDto();
        dto.favoriteId = favorite.getId();
        dto.cat = favorite.getCat();
        dto.memberId = favorite.getMember().getId();
        return dto;
    }
}

