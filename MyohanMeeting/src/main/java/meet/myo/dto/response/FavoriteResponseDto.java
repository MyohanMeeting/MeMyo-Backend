package meet.myo.dto.response;

import meet.myo.domain.Favorite;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import meet.myo.dto.response.adopt.AdoptNoticeSummaryResponseDto;


@Schema(name = "Favorite")
@Getter
public class FavoriteResponseDto {
    private Long favoriteId;
    private Long memberId;
    private AdoptNoticeSummaryResponseDto notice;


    public static FavoriteResponseDto fromEntity(Favorite favorite) {
        FavoriteResponseDto dto = new FavoriteResponseDto();
        dto.favoriteId = favorite.getId();
        dto.notice = AdoptNoticeSummaryResponseDto.fromEntity(favorite.getAdoptNotice());
        dto.memberId = favorite.getMember().getId();

        return dto;
    }
}
