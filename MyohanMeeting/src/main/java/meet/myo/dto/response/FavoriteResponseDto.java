package meet.myo.dto.response;

import meet.myo.domain.Favorite;
import meet.myo.domain.Member;
import meet.myo.domain.cat.Cat;

public class FavoriteResponseDto {
    private Long favoriteId;
    private Long memberId;
    private Cat cat; // 이렇게 엔티티로 받으면 안돼

    public static FavoriteResponseDto fromEntity(Favorite favorite) {
        FavoriteResponseDto dto = new FavoriteResponseDto();
        dto.favoriteId = favorite.getId();
        dto.cat = favorite.getCat();
        dto.memberId = favorite.getMember().getId();
        return dto;
    }
}

