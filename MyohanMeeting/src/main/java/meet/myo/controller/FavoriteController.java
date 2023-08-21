package meet.myo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import meet.myo.config.SecurityUtil;
import meet.myo.dto.request.CreateFavoriteRequestDto;
import meet.myo.dto.response.CommonResponseDto;
import meet.myo.dto.response.FavoriteResponseDto;
import meet.myo.exception.NotAuthenticatedException;
import meet.myo.service.FavoriteService;
import meet.myo.springdoc.annotations.ApiResponseAuthority;
import meet.myo.springdoc.annotations.ApiResponseCommon;
import meet.myo.springdoc.annotations.ApiResponseResource;
import meet.myo.springdoc.annotations.ApiResponseSignin;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "6. Favorite", description = "최애친구 관련 기능")
@SecurityRequirement(name = "JWT")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/favorite")
@PreAuthorize("hasAnyRole('ROLE_USER')")
public class FavoriteController {

    private final FavoriteService favoriteService;

    /**
     * 최애친구 목록 조회
     */
    @Operation(summary = "최애친구 목록 조회",
            description = "자신의 최애친구 목록을 조회합니다.", operationId = "getMyFavoriteList")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseSignin
    @GetMapping("")
    public CommonResponseDto<List<FavoriteResponseDto>> getMyFavoriteListV1(
            /**
             * 페이징
             */
            @Parameter(name = "page", description = "페이지를 설정합니다.", in = ParameterIn.QUERY)
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,

            @Parameter(name = "limit", description = "리미트를 설정합니다.", in = ParameterIn.QUERY)
            @RequestParam(value = "limit", required = false, defaultValue = "100") int size
    ) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID_ID"));
        Pageable pageable = PageRequest.of(page, size);
        return CommonResponseDto.<List<FavoriteResponseDto>>builder()
                .data(favoriteService.getFavoriteList(memberId, pageable))
                .build();
    }

    /**
     * 최애친구 작성
     */
    @Operation(summary = "최애친구 작성",
            description = "하트를 클릭해 최애친구를 생성합니다.", operationId = "createFavorite")
    @ApiResponse(responseCode = "200", description = "작성 성공", content = @Content(
            schema = @Schema(implementation = CommonResponseDto.class), examples = { @ExampleObject(value = """
{
  "status": "200 OK",
  "timestamp": "2023-06-10T09:19:08.550Z",
  "message": "SUCCESS",
  "data": {
    "favoriteId" : 1
  }
}
""")})) @ApiResponseCommon @ApiResponseResource @ApiResponseSignin
    @PostMapping("")
    public CommonResponseDto<Map<String, Long>> createFavoriteV1(
            @Validated @RequestBody final CreateFavoriteRequestDto dto
    ) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID_ID"));
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("favoriteId", favoriteService.createFavorite(memberId, dto)))
                .build();
    }

    /**
     * 최애친구 삭제
     */
    @Operation(summary = "최애친구 삭제",
            description = "하트를 클릭해 최애친구를 삭제합니다.", operationId = "deleteFavorite")
    @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(
            schema = @Schema(implementation = CommonResponseDto.class), examples = { @ExampleObject(value = """
{
  "status": "200 OK",
  "timestamp": "2023-06-10T09:19:08.550Z",
  "message": "SUCCESS",
  "data": {
    "favoriteId" : 1
  }
}
""")})) @ApiResponseCommon @ApiResponseResource @ApiResponseAuthority
    @DeleteMapping("/{favoriteId}")
    public CommonResponseDto<Map<String, Long>> deleteFavoriteV1(
            @Parameter(name = "favoriteId", description = "삭제하고자 하는 최애친구의 id입니다.")
            @PathVariable(name = "favoriteId") Long favoriteId
    ) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID_ID"));
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("favoriteId", favoriteService.deleteFavorite(memberId, favoriteId)))
                .build();
    }
}
