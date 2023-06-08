package meet.myo.controller;

import lombok.RequiredArgsConstructor;
import meet.myo.dto.request.CreateFavoriteRequestDto;
import meet.myo.dto.response.CommonResponseDto;
import meet.myo.dto.response.FavoriteResponseDto;
import meet.myo.service.FavoriteService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/favorite")
public class FavoriteController {

    private final FavoriteService favoriteService;

    /**
     * 최애친구 목록 조회
     */
    @GetMapping("")
    public CommonResponseDto<List<FavoriteResponseDto>> getMyFavoriteListV1(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "100") int size
    ) {
        Long memberId = 1L; //TODO: security
        Pageable pageable = PageRequest.of(page, size);
        return CommonResponseDto.<List<FavoriteResponseDto>>builder()
                .data(favoriteService.getFavoriteList(memberId, pageable))
                .build();
    }

    /**
     * 최애친구 작성
     */
    @PostMapping("")
    public CommonResponseDto<Map<String, Long>> getFavoriteV1(
            @Validated @RequestBody final CreateFavoriteRequestDto dto
    ) {
        Long memberId = 1L; //TODO: security
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("favoriteId", favoriteService.createFavorite(memberId, dto)))
                .build();
    }

    /**
     * 최애친구 삭제
     */
    @DeleteMapping("/{favoriteId}")
    public CommonResponseDto<Map<String, Long>> deleteFavoriteV1(
            @PathVariable(name = "favoriteId") Long favoriteId
    ) {
        Long memberId = 1L; //TODO: security
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("favoriteId", favoriteService.deleteFavorite(memberId, favoriteId)))
                .build();
    }
}
