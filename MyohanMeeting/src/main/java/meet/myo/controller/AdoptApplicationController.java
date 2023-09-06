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
import meet.myo.dto.request.adopt.AdoptApplicationCreateRequestDto;
import meet.myo.dto.request.adopt.AdoptApplicationUpdateRequestDto;
import meet.myo.dto.response.adopt.AdoptApplicationResponseDto;
import meet.myo.dto.response.CommonResponseDto;
import meet.myo.exception.NotAuthenticatedException;
import meet.myo.service.AdoptApplicationService;
import meet.myo.springdoc.annotations.ApiResponseAuthority;
import meet.myo.springdoc.annotations.ApiResponseCommon;
import meet.myo.springdoc.annotations.ApiResponseResource;
import meet.myo.springdoc.annotations.ApiResponseSignin;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "4. Adopt Application", description = "분양신청 관련 기능")
@SecurityRequirement(name = "Authorization")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/adoption/applications")
@PreAuthorize("hasAnyRole('ROLE_USER')")
public class AdoptApplicationController {

    private final AdoptApplicationService adoptApplicationService;

    /**
     * 특정 분양신청 상세조회
     */
    @Operation(summary = "특정 분양신청 상세조회",
            description = "개별 분양신청의 상세 내용을 조회합니다.", operationId = "getApplication")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseResource @ApiResponseAuthority
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
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseSignin
    @GetMapping("/my")
    public CommonResponseDto<List<AdoptApplicationResponseDto>> getMyApplicationListV1(
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
        return CommonResponseDto.<List<AdoptApplicationResponseDto>>builder()
                .data(adoptApplicationService.getMyAdoptApplicationList(memberId, pageable))
                .build();
    }

    /**
     * 특정 공고에 달린 신청 모아보기(공고 작성자만 조회가능)
     */
    @Operation(summary = "특정 공고에 달린 신청 모아보기",
            description = "개별 공고에 달린 신청을 한번에 모아봅니다. 해당 공고의 작성자만 열람 가능합니다.",
            operationId = "getApplicationListByNotice")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseResource @ApiResponseAuthority
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
            @RequestParam(value = "limit", required = false, defaultValue = "100") int size
    ) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID_ID"));
        Pageable pageable = PageRequest.of(page, size);
        return CommonResponseDto.<List<AdoptApplicationResponseDto>>builder()
                .data(adoptApplicationService.getAdoptApplicationListByNotice(memberId, noticeId, pageable))
                .build();
    }

    /**
     * 분양신청 작성
     */
    @Operation(summary = "분양신청 작성", description = "분양 신청서를 작성합니다.", operationId = "createApplication")
    @ApiResponse(responseCode = "200", description = "작성 성공", content = @Content(
            schema = @Schema(implementation = CommonResponseDto.class), examples = { @ExampleObject(value = """
{
  "status": "200 OK",
  "timestamp": "2023-06-10T09:19:08.550Z",
  "message": "SUCCESS",
  "data": {
    "applicationId" : 1
  }
}
""")})) @ApiResponseCommon @ApiResponseResource @ApiResponseSignin
    @PostMapping("")
    public CommonResponseDto<Map<String, Long>> createApplicationV1(
            @Valid @RequestBody final AdoptApplicationCreateRequestDto dto
    ) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID_ID"));
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("applicationId", adoptApplicationService.createAdoptApplication(memberId, dto)))
                .build();
    }

    /**
     * 분양신청 수정
     */
    @Operation(summary = "분양신청 수정", description = "분양 신청서를 수정합니다.", operationId = "updateApplication")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseResource @ApiResponseAuthority
    @PatchMapping("/{applicationId}")
    public CommonResponseDto<AdoptApplicationResponseDto> updateApplicationV1(
            @Parameter(name = "applicationId", description = "수정하고자 하는 분양신청의 id입니다.")
            @PathVariable(name = "applicationId") Long applicationId,

            @RequestBody final AdoptApplicationUpdateRequestDto dto
    ) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID_ID"));
        return CommonResponseDto.<AdoptApplicationResponseDto>builder()
                .data(adoptApplicationService.updateAdoptApplication(memberId, applicationId, dto))
                .build();
    }

    /**
     * 분양신청 삭제
     */
    @Operation(summary = "분양신청 삭제", description = "분양 신청서를 삭제합니다.", operationId = "deleteApplication")
    @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(
            schema = @Schema(implementation = CommonResponseDto.class), examples = { @ExampleObject(value = """
{
  "status": "200 OK",
  "timestamp": "2023-06-10T09:19:08.550Z",
  "message": "SUCCESS",
  "data": {
    "applicationId" : 1
  }
}
""")})) @ApiResponseCommon @ApiResponseResource @ApiResponseAuthority
    @DeleteMapping("/{applicationId}")
    public CommonResponseDto<Map<String, Long>> deleteApplicationV1(
            @Parameter(name = "applicationId", description = "삭제하고자 하는 분양신청의 id입니다.")
            @PathVariable(name = "applicationId") Long applicationId
    ) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID_ID"));
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("applicationId", adoptApplicationService.deleteAdoptApplication(memberId, applicationId)))
                .build();
    }

}
