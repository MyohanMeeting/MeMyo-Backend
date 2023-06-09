package meet.myo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import meet.myo.dto.request.*;
import meet.myo.dto.response.AdoptNoticeCommentResponseDto;
import meet.myo.dto.response.AdoptNoticeResponseDto;
import meet.myo.dto.response.CommonResponseDto;
import meet.myo.search.AdoptNoticeSearch;
import meet.myo.service.AdoptNoticeService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/adoption/notices")
public class AdoptNoticeController {

    private final AdoptNoticeService adoptNoticeService;

    /**
     * 분양공고 목록조회
     */
    @Tag(name = "Adopt Notice", description = "분양공고 관련 기능")
    @Operation(summary = "분양공고 목록조회", description = "검색 조건에 따른 분양 공고 목록을 조회합니다.", operationId = "getNoticeList")
    @GetMapping("")
    public CommonResponseDto<List<AdoptNoticeResponseDto>> getNoticeListV1(
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

        return CommonResponseDto.<List<AdoptNoticeResponseDto>>builder()
                .data(adoptNoticeService.getAdoptNoticeList(pageable, search))
                .build();
    }

    /**
     * 내가 올린 분양공고 목록조회
     */
    @Tag(name = "Adopt Notice", description = "분양공고 관련 기능")
    @Operation(summary = "내가 올린 분양공고 목록조회", description = "자신이 업로드한 분양 공고 목록을 조회합니다.", operationId = "getMyNoticeList")
    @GetMapping("/my")
    public CommonResponseDto<List<AdoptNoticeResponseDto>> getMyNoticeListV1(
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
        Long memberId = 1L; // TODO: security
        Pageable pageable = PageRequest.of(page, size);
        return CommonResponseDto.<List<AdoptNoticeResponseDto>>builder()
                .data(adoptNoticeService.getMyAdoptNoticeList(memberId, pageable, sort))
                .build();
    }

    /**
     * 분양공고 상세조회
     */
    @Tag(name = "Adopt Notice", description = "분양공고 관련 기능")
    @Operation(summary = "분양공고 상세조회", description = "개별 분양공고의 상세 내용을 조회합니다.", operationId = "getNotice")
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
    @Tag(name = "Adopt Notice", description = "분양공고 관련 기능")
    @Operation(summary = "분양공고 작성", description = "분양공고를 작성합니다.", operationId = "createNotice")
    @SecurityRequirement(name = "JWT")
    @PostMapping("")
    public CommonResponseDto<Map<String, Long>> createNoticeV1(@Validated @RequestBody final AdoptNoticeCreateRequestDto dto) {
        Long memberId = 1L; //TODO: security
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("noticeId", adoptNoticeService.createAdoptNotice(memberId, dto)))
                .build();
    }

    /**
     * 분양공고 상태 업데이트
     */
    @Tag(name = "Adopt Notice", description = "분양공고 관련 기능")
    @Operation(summary = "분양공고 상태 업데이트", description = "분양공고의 상태를 업데이트합니다.", operationId = "updateNoticeStatus")
    @SecurityRequirement(name = "JWT")
    @PatchMapping("/status/{noticeId}")
    public CommonResponseDto<AdoptNoticeResponseDto> updateNoticeStatusV1(
            @Parameter(name = "noticeId", description = "수정하고자 하는 공고의 id입니다.")
            @PathVariable(name = "noticeId") Long noticeId,

            @Validated @RequestBody final AdoptNoticeStatusUpdateRequestDto dto
    ) {
        Long memberId = 1L; //TODO: security
        return CommonResponseDto.<AdoptNoticeResponseDto>builder()
                .data(adoptNoticeService.updateAdoptNoticeStatus(memberId, noticeId, dto))
                .build();
    }

    /**
     * 분양공고 수정
     */
    @Tag(name = "Adopt Notice", description = "분양공고 관련 기능")
    @Operation(summary = "분양공고 수정", description = "분양공고의 내용을 수정합니다.", operationId = "updateNotice")
    @PatchMapping("/{noticeId}")
    @SecurityRequirement(name = "JWT")
    public CommonResponseDto<AdoptNoticeResponseDto> updateNoticeV1(
            @Parameter(name = "noticeId", description = "수정하고자 하는 공고의 id입니다.")
            @PathVariable(name = "noticeId") Long noticeId,

            @Validated @RequestBody final AdoptNoticeUpdateRequestDto dto
    ) {
        Long memberId = 1L; //TODO: security
        return CommonResponseDto.<AdoptNoticeResponseDto>builder()
                .data(adoptNoticeService.updateAdoptNotice(memberId, noticeId, dto))
                .build();
    }

    /**
     * 분양공고 삭제
     */
    @Tag(name = "Adopt Notice", description = "분양공고 관련 기능")
    @Operation(summary = "분양공고 삭제", description = "분양공고를 삭제합니다.", operationId = "deleteNotice")
    @SecurityRequirement(name = "JWT")
    @DeleteMapping("/{noticeId}")
    public CommonResponseDto<Map<String, Long>> deleteNoticeV1(
            @Parameter(name = "noticeId", description = "삭제하고자 하는 공고의 id입니다.")
            @PathVariable(name = "noticeId") Long noticeId
    ) {
        Long memberId = 1L; //TODO: security
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("noticeId", adoptNoticeService.deleteAdoptNotice(memberId, noticeId)))
                .build();
    }

    /**
     * 분양공고에 달린 댓글목록 조회
     */
    @Tag(name = "Adopt Notice Comment", description = "분양공고 댓글 관련 기능")
    @Operation(summary = "분양공고 댓글 조회", description = "특정 분양공고에 달린 댓글을 조회합니다.", operationId = "getNoticeCommentList")
    @GetMapping("/{noticeId}/comments")
    public CommonResponseDto<List<AdoptNoticeCommentResponseDto>> getNoticeCommentListV1(
            @Parameter(name = "noticeId", description = "댓글을 조회하고자 하는 공고의 id입니다.")
            @PathVariable(name = "noticeId") Long noticeId,
            @Validated @RequestBody final AdoptNoticeCommentCreateRequestDto dto
    ) {
        return CommonResponseDto.<List<AdoptNoticeCommentResponseDto>>builder()
                .data(adoptNoticeService.getAdoptNoticeCommentList(noticeId))
                .build();
    }

    /**
     * 분양공고 댓글 작성
     */
    @Tag(name = "Adopt Notice Comment", description = "분양공고 댓글 관련 기능")
    @Operation(summary = "분양공고 댓글 작성", description = "분양공고 댓글을 작성합니다.", operationId = "createNoticeComment")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/comments")
    public CommonResponseDto<Map<String, Long>> createNoticeCommentV1(
            @Validated @RequestBody final AdoptNoticeCommentCreateRequestDto dto
    ) {
        Long memberId = 1L; //TODO: security
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("noticeId", adoptNoticeService.createAdoptNoticeComment(memberId, dto)))
                .build();
    }

    /**
     * 분양공고 댓글 수정
     */
    @Tag(name = "Adopt Notice Comment", description = "분양공고 댓글 관련 기능")
    @Operation(summary = "분양공고 댓글 수정", description = "분양공고 댓글의 내용을 수정합니다.", operationId = "updateNoticeCommentV1")
    @PatchMapping("/comments/{noticeCommentId}")
    @SecurityRequirement(name = "JWT")
    public CommonResponseDto<AdoptNoticeCommentResponseDto> updateNoticeCommentV1(
            @Parameter(name = "noticeCommentId", description = "수정하고자 하는 공고의 id입니다.")
            @PathVariable(name = "noticeCommentId") Long noticeCommentId,

            @Validated @RequestBody final AdoptNoticeCommentUpdateRequestDto dto
    ) {
        Long memberId = 1L; //TODO: security
        return CommonResponseDto.<AdoptNoticeCommentResponseDto>builder()
                .data(adoptNoticeService.updateAdoptNoticeComment(memberId, noticeCommentId, dto))
                .build();
    }

    /**
     * 분양공고 댓글 삭제
     */
    @Tag(name = "Adopt Notice Comment", description = "분양공고 댓글 관련 기능")
    @Operation(summary = "분양공고 댓글 삭제", description = "분양공고 댓글을 삭제합니다.", operationId = "deleteNoticeComment")
    @SecurityRequirement(name = "JWT")
    @DeleteMapping("/comments/{noticeCommentId}")
    public CommonResponseDto<Map<String, Long>> deleteNoticeCommentV1(
            @Parameter(name = "noticeCommentId", description = "삭제하고자 하는 댓글의 id입니다.")
            @PathVariable(name = "noticeCommentId") Long noticeCommentId
    ) {
        Long memberId = 1L; //TODO: security
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("noticeCommentId", adoptNoticeService.deleteAdoptNoticeComment(memberId, noticeCommentId)))
                .build();
    }
}
