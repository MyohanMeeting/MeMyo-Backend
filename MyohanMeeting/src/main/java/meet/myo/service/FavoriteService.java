package meet.myo.service;

import lombok.RequiredArgsConstructor;
import meet.myo.dto.request.CreateFavoriteRequestDto;
import meet.myo.dto.response.FavoriteResponseDto;
import meet.myo.repository.FavoriteRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    public List<FavoriteResponseDto> getFavoriteList(Long memberId, Pageable pageable) {
        return List.of(FavoriteResponseDto.fromEntity());
    }

    public Long createFavorite(Long memberId, CreateFavoriteRequestDto dto) {
        return 1L;
    }

    public Long deleteFavorite(Long memberId, Long favoriteId) {
        return 1L;
    }
}
