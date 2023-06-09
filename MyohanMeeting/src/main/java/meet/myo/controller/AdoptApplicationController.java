package meet.myo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import meet.myo.dto.request.AdoptApplicationCreateRequestDto;
import meet.myo.dto.request.AdoptApplicationUpdateRequestDto;
import meet.myo.dto.response.AdoptApplicationResponseDto;
import meet.myo.dto.response.CommonResponseDto;
import meet.myo.service.AdoptApplicationService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Adopt Application", description = "분양신청 관련 기능")
@SecurityRequirement(name = "JWT")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/adoption/applications")
public class AdoptApplicationController {

    private final AdoptApplicationService adoptApplicationService;

    /**
     * 특정 분양신청 상세조회
     */
    @Operation(summary = "특정 분양신청 상세조회",
            description = "개별 분양신청의 상세 내용을 조회합니다.", operationId = "getApplication")
    @GetMapping("/{applicationId}")
    public CommonResponseDto<AdoptApplicationResponseDto> getApplicationV1(
            @Parameter(name = "applicationId", description = "조회하고자 하는 분양신청의 id입니다.")
            @PathVariable(name = "applicationId") Long applicationId
    ) {
        return CommonResponseDto.<AdoptApplicationResponseDto>builder()
                .data(adoptApplicationService.getAdoptApplication(applicationId))
                .build();
    }

    /**
     * 내가 한 분양신청 목록조회
     */
    @Operation(summary = "내가 한 분양신청 목록조회",
            description = "자신이 작성한 분양신청 목록을 조회합니다.",
            operationId = "getMyApplicationList")
    @GetMapping("/my")
    public CommonResponseDto<List<AdoptApplicationResponseDto>> getMyApplicationListV1(
            /**
             * 페이징
             */
            @Parameter(name = "page", description = "페이지를 설정합니다.", in = ParameterIn.QUERY)
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,

            @Parameter(name = "limit", description = "리미트를 설정합니다.", in = ParameterIn.QUERY)
            @RequestParam(value = "limit", required = false, defaultValue = "100") int size,

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
        return CommonResponseDto.<List<AdoptApplicationResponseDto>>builder()
                .data(adoptApplicationService.getMyAdoptApplicationList(memberId, pageable, sort))
                .build();
    }

    /**
     * 특정 공고에 달린 신청 모아보기(공고 작성자만 조회가능)
     */
    @Operation(summary = "특정 공고에 달린 신청 모아보기",
            description = "개별 공고에 달린 신청을 한번에 모아봅니다. 해당 공고의 작성자만 열람 가능합니다.",
            operationId = "getApplicationListByNotice")
    @GetMapping("/byNotice/{noticeId}")
    public CommonResponseDto<List<AdoptApplicationResponseDto>> getApplicationListByNoticeV1(
            @Parameter(name = "noticeId", description = "신청 목록을 조회하고자 하는 분양공고의 id입니다.")
            @PathVariable(name = "noticeId") Long noticeId,

            /**
             * 페이징
             */
            @Parameter(name = "page", description = "페이지를 설정합니다.", in = ParameterIn.QUERY)
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,

            @Parameter(name = "limit", description = "리미트를 설정합니다.", in = ParameterIn.QUERY)
            @RequestParam(value = "limit", required = false, defaultValue = "100") int size,

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
        return CommonResponseDto.<List<AdoptApplicationResponseDto>>builder()
                .data(adoptApplicationService.getAdoptApplicationListByNotice(memberId, noticeId, pageable, sort))
                .build();
    }

    /**
     * 분양신청 작성
     */
    @Operation(summary = "분양신청 작성", description = "분양 신청서를 작성합니다.", operationId = "createApplication")
    @PostMapping("")
    public CommonResponseDto<Map<String, Long>> createApplicationV1(
            @Validated @RequestBody final AdoptApplicationCreateRequestDto dto
    ) {
        Long memberId = 1L; // TODO: security
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("applicationId", adoptApplicationService.createAdoptApplication(memberId, dto)))
                .build();
    }

    /**
     * 분양신청 수정
     */
    @Operation(summary = "분양신청 수정", description = "분양 신청서를 수정합니다.", operationId = "updateApplication")
    @PatchMapping("/{applicationId}")
    public CommonResponseDto<AdoptApplicationResponseDto> updateApplicationV1(
            @Parameter(name = "applicationId", description = "수정하고자 하는 분양신청의 id입니다.")
            @PathVariable(name = "applicationId") Long applicationId,

            @Validated @RequestBody final AdoptApplicationUpdateRequestDto dto
    ) {
        Long memberId = 1L; //TODO: security
        return CommonResponseDto.<AdoptApplicationResponseDto>builder()
                .data(adoptApplicationService.updateAdoptApplication(memberId, applicationId, dto))
                .build();
    }

    /**
     * 분양신청 삭제
     */
    @Operation(summary = "분양신청 삭제", description = "분양 신청서를 삭제합니다.", operationId = "deleteApplication")
    @DeleteMapping("/{applicationId}")
    public CommonResponseDto<Map<String, Long>> deleteApplicationV1(
            @Parameter(name = "applicationId", description = "삭제하고자 하는 분양신청의 id입니다.")
            @PathVariable(name = "applicationId") Long applicationId
    ) {
        Long memberId = 1L; //TODO: security
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("applicationId", adoptApplicationService.deleteAdoptApplication(memberId, applicationId)))
                .build();
    }

}
