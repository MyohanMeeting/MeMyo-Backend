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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import meet.myo.config.SecurityUtil;
import meet.myo.dto.request.adopt.AdoptNoticeCreateRequestDto;
import meet.myo.dto.request.adopt.AdoptNoticeUpdateRequestDto;
import meet.myo.dto.response.adopt.AdoptNoticeResponseDto;
import meet.myo.dto.response.CommonResponseDto;
import meet.myo.dto.response.adopt.AdoptNoticeSummaryResponseDto;
import meet.myo.exception.NotAuthenticatedException;
import meet.myo.search.AdoptNoticeSearch;
import meet.myo.service.AdoptNoticeService;
import meet.myo.springdoc.annotations.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "3. Adopt Notice", description = "분양공고 관련 기능")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/adoption/notices")
public class AdoptNoticeController {

    private final AdoptNoticeService adoptNoticeService;

    /**
     * 분양공고 목록조회
     */
    @Operation(summary = "분양공고 목록조회", description = "검색 조건에 따른 분양 공고 목록을 조회합니다.", operationId = "getNoticeList")
    @ApiResponse(responseCode = "200") @ApiResponseCommon
    @GetMapping("")
    public CommonResponseDto<List<AdoptNoticeSummaryResponseDto>> getNoticeListV1(
            /**
             * 페이징
             */
            @Parameter(name = "page", description = "페이지를 설정합니다.", in = ParameterIn.QUERY)
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,

            @Parameter(name = "limit", description = "리미트를 설정합니다.", in = ParameterIn.QUERY)
            @RequestParam(value = "limit", required = false, defaultValue = "100") int size,

            /**
             * 검색용 파라미터
             */
            // 공고 내용 관련 파라미터
            @Parameter(name = "title", description = "제목으로 검색합니다.", in = ParameterIn.QUERY)
            @RequestParam(value = "title", required = false) String title,

            @Parameter(name = "content", description = "내용으로 검색합니다.", in = ParameterIn.QUERY)
            @RequestParam(value = "content", required = false) String content,

            @Parameter(name = "authorName", description = "작성자명(닉네임)으로 검색합니다.", in = ParameterIn.QUERY)
            @RequestParam(value = "authorName", required = false) String authorName,

            @Parameter(name = "noticeStatus", description = "공고의 상태로 검색합니다.", in = ParameterIn.QUERY)
            @RequestParam(value = "noticeStatus", required = false) String noticeStatus,

            // 고양이 정보 관련 파라미터
            @Parameter(name = "catName", description = "고양이 이름으로 검색합니다.", in = ParameterIn.QUERY)
            @RequestParam(value = "catName", required = false) String catName,

            @Parameter(name = "catSpecies", description = "고양이 종으로 검색합니다.", in = ParameterIn.QUERY)
            @RequestParam(value = "catSpecies", required = false) String catSpecies,

            @Parameter(name = "catAge", description = "고양이 나이로 검색합니다.", in = ParameterIn.QUERY)
            @RequestParam(value = "catAge", required = false) String catAge,

            @Parameter(name = "catSex", description = "고양이 성별로 검색합니다.", in = ParameterIn.QUERY)
            @RequestParam(value = "catSex", required = false) String catSex,

            @Parameter(name = "neutered", description = "중성화 여부로 검색합니다.", in = ParameterIn.QUERY)
            @RequestParam(value = "neutered", required = false) String neutered,

            // 보호소 정보 관련 파라미터
            @Parameter(name = "city", description = "보호소 위치로 검색합니다. 지역 코드는 아래와 같습니다.",
                    in = ParameterIn.QUERY, schema = @Schema(description = "City Enum Value",
                    allowableValues = {"SEOUL", "SEJONG", "BUSAN", "DAEGU", "INCHEON", "GWANGJU", "ULSAN", "DAEJEON",
                            "GYEONGGI", "GANGWON", "CHUNGCHEONG_BUK", "CHUNGCHEONG_NAM", "JEOLLA_BUK", "JEOLLA_NAM",
                            "GYEONGSANG_BUK", "GYEONGSANG_NAM", "JEJU"}))
            @RequestParam(value = "city", required = false) String city,

            @Parameter(name = "shelterName", description = "보호소 이름으로 검색합니다.", in = ParameterIn.QUERY)
            @RequestParam(value = "shelterName", required = false) String shelterName,

            /**
             * 정렬 기준 파라미터
             */
            @Parameter(name = "sort", description = "정렬 기준을 선택합니다.<br><br>" +
                    ", 를 구분자로 항목 앞에 -를 붙이면 내림차순, 붙이지 않으면 오름차순 정렬입니다.<br><br>" +
                    "정렬 가능한 항목은 다음과 같습니다: createdAt(작성일), applicationCount(신청수), commentCount(댓글수)",
                    in = ParameterIn.QUERY, example = "-createdAt,applicationCount")
            @RequestParam(value = "sort", required = false) String sort
    ) { //TODO: 이부분 들여쓰기 컨벤션 찾아보기

        Pageable pageable = PageRequest.of(page, size);
        AdoptNoticeSearch search = AdoptNoticeSearch.builder()
                // 공고 정보
                .title(title)
                .content(content)
                .authorName(authorName)
                .noticeStatus(noticeStatus)

                // 고양이 정보
                .catName(catName)
                .catSpecies(catSpecies)
                .catAge(catAge)
                .catSex(catSex)
                .neutered(neutered)

                // 보호소 정보
                .city(city)
                .shelterName(shelterName)

                // 정렬
                .sort(sort)
                .build();

        return CommonResponseDto.<List<AdoptNoticeSummaryResponseDto>>builder()
                .data(adoptNoticeService.getAdoptNoticeList(pageable, search))
                .build();
    }

    /**
     * 내가 올린 분양공고 목록조회
     */
    @Operation(summary = "내가 올린 분양공고 목록조회", description = "자신이 업로드한 분양 공고 목록을 조회합니다.", operationId = "getMyNoticeList")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseSignin
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping("/my")
    public CommonResponseDto<List<AdoptNoticeSummaryResponseDto>> getMyNoticeListV1(
            /**
             * 페이징
             */
            @Parameter(name = "page", description = "페이지를 설정합니다.", in = ParameterIn.QUERY)
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,

            @Parameter(name = "limit", description = "리미트를 설정합니다.", in = ParameterIn.QUERY)
            @RequestParam(value = "limit", required = false, defaultValue = "100") int size,

            /**
             * 검색용 파라미터
             */
            @Parameter(name = "noticeStatus", description = "공고의 상태로 검색합니다.", in = ParameterIn.QUERY)
            @RequestParam(value = "noticeStatus", required = false) String noticeStatus, //TODO: 서비스레이어 메서드에 인자로 추가

            /**
             * 정렬 기준 파라미터
             */
            @Parameter(name = "sort", description = "정렬 기준을 선택합니다.<br><br>" +
                    ", 를 구분자로 항목 앞에 -를 붙이면 내림차순, 붙이지 않으면 오름차순 정렬입니다.<br><br>" +
                    "정렬 가능한 항목은 다음과 같습니다: createdAt(작성일), applicationCount(신청수), commentCount(댓글수)",
                    in = ParameterIn.QUERY, example = "-createdAt,applicationCount")
            @RequestParam(value = "sort", required = false) String sort
    ) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID_ID"));
        Pageable pageable = PageRequest.of(page, size);
        return CommonResponseDto.<List<AdoptNoticeSummaryResponseDto>>builder()
                .data(adoptNoticeService.getMyAdoptNoticeList(memberId, pageable, sort))
                .build();
    }

    /**
     * 분양공고 상세조회
     */
    @Operation(summary = "분양공고 상세조회", description = "개별 분양공고의 상세 내용을 조회합니다.", operationId = "getNotice")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseResource
    @GetMapping("/{noticeId}")
    public CommonResponseDto<AdoptNoticeResponseDto> getNoticeV1(
            @Parameter(name = "noticeId", description = "조회하고자 하는 공고의 id입니다.")
            @PathVariable(name = "noticeId") Long noticeId
    ) {
        return CommonResponseDto.<AdoptNoticeResponseDto>builder()
                .data(adoptNoticeService.getAdoptNotice(noticeId))
                .build();
    }

    /**
     * 분양공고 작성
     */
    @Operation(summary = "분양공고 작성", description = "분양공고를 작성합니다.", operationId = "createNotice")
    @ApiResponse(responseCode = "200", description = "작성 성공", content = @Content(
            schema = @Schema(implementation = CommonResponseDto.class), examples = { @ExampleObject(value = """
{
  "status": "200 OK",
  "timestamp": "2023-06-10T09:19:08.550Z",
  "message": "SUCCESS",
  "data": {
    "noticeId" : 1
  }
}
""")})) @ApiResponseCommon @ApiResponseSignin
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @SecurityRequirement(name = "Authorization")
    @PostMapping("")
    public CommonResponseDto<Map<String, Long>> createNoticeV1(@Valid @RequestBody final AdoptNoticeCreateRequestDto dto) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID_ID"));
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("noticeId", adoptNoticeService.createAdoptNotice(memberId, dto)))
                .build();
    }

    /**
     * 분양공고 상태 업데이트
     */
    @Operation(summary = "분양공고 상태 업데이트", description = "분양공고의 상태를 업데이트합니다.", operationId = "updateNoticeStatus")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseResource @ApiResponseAuthority
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @SecurityRequirement(name = "Authorization")
    @PutMapping("/{noticeId}/status")
    public CommonResponseDto<AdoptNoticeResponseDto> updateNoticeStatusV1(
            @Parameter(name = "noticeId", description = "수정하고자 하는 공고의 id입니다.")
            @PathVariable(name = "noticeId") Long noticeId,

            @Schema(allowableValues = {"ACCEPTING", "COMPLETE", "CANCELED"})
            @Parameter(name = "status", description = "수정하고자 하는 공고의 상태입니다.", in = ParameterIn.QUERY)
            @RequestParam(name = "status") String status
    ) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID_ID"));
        return CommonResponseDto.<AdoptNoticeResponseDto>builder()
                .data(adoptNoticeService.updateAdoptNoticeStatus(memberId, noticeId, status))
                .build();
    }

    /**
     * 분양공고 수정
     */
    @Operation(summary = "분양공고 수정", description = "분양공고의 내용을 수정합니다.", operationId = "updateNotice")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseResource @ApiResponseAuthority
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @SecurityRequirement(name = "Authorization")
    @PatchMapping("/{noticeId}")
    public CommonResponseDto<AdoptNoticeResponseDto> updateNoticeV1(
            @Parameter(name = "noticeId", description = "수정하고자 하는 공고의 id입니다.")
            @PathVariable(name = "noticeId") Long noticeId,

            @RequestBody final AdoptNoticeUpdateRequestDto dto
    ) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID_ID"));
        return CommonResponseDto.<AdoptNoticeResponseDto>builder()
                .data(adoptNoticeService.updateAdoptNotice(memberId, noticeId, dto))
                .build();
    }

    /**
     * 분양공고 삭제
     */
    @Operation(summary = "분양공고 삭제", description = "분양공고를 삭제합니다.", operationId = "deleteNotice")
    @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(
            schema = @Schema(implementation = CommonResponseDto.class), examples = { @ExampleObject(value = """
{
  "status": "200 OK",
  "timestamp": "2023-06-10T09:19:08.550Z",
  "message": "SUCCESS",
  "data": {
    "noticeId" : 1
  }
}
""")})) @ApiResponseCommon @ApiResponseResource @ApiResponseAuthority
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @SecurityRequirement(name = "Authorization")
    @DeleteMapping("/{noticeId}")
    public CommonResponseDto<Map<String, Long>> deleteNoticeV1(
            @Parameter(name = "noticeId", description = "삭제하고자 하는 공고의 id입니다.")
            @PathVariable(name = "noticeId") Long noticeId
    ) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID_ID"));
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("noticeId", adoptNoticeService.deleteAdoptNotice(memberId, noticeId)))
                .build();
    }
}
