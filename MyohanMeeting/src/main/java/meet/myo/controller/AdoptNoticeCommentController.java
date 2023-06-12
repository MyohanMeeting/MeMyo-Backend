package meet.myo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import meet.myo.dto.request.adopt.AdoptNoticeCommentCreateRequestDto;
import meet.myo.dto.request.adopt.AdoptNoticeCommentUpdateRequestDto;
import meet.myo.dto.response.adopt.AdoptNoticeCommentResponseDto;
import meet.myo.dto.response.CommonResponseDto;
import meet.myo.service.AdoptNoticeService;
import meet.myo.springdoc.annotations.ApiResponseAuthority;
import meet.myo.springdoc.annotations.ApiResponseCommon;
import meet.myo.springdoc.annotations.ApiResponseResource;
import meet.myo.springdoc.annotations.ApiResponseSignin;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Adopt Notice Comment", description = "분양공고 댓글 관련 기능")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/adoption/notices")
public class AdoptNoticeCommentController {

    private final AdoptNoticeService adoptNoticeService;

    /**
     * 분양공고에 달린 댓글목록 조회
     */
    @Operation(summary = "분양공고 댓글 조회", description = "특정 분양공고에 달린 댓글을 조회합니다.", operationId = "getNoticeCommentList")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseResource
    @GetMapping("/{noticeId}/comments")
    public CommonResponseDto<List<AdoptNoticeCommentResponseDto>> getNoticeCommentListV1(
            @Parameter(name = "noticeId", description = "댓글을 조회하고자 하는 공고의 id입니다.")
            @PathVariable(name = "noticeId") Long noticeId
    ) {
        return CommonResponseDto.<List<AdoptNoticeCommentResponseDto>>builder()
                .data(adoptNoticeService.getAdoptNoticeCommentList(noticeId))
                .build();
    }

    /**
     * 분양공고 댓글 작성
     */
    @Operation(summary = "분양공고 댓글 작성", description = "분양공고 댓글을 작성합니다.", operationId = "createNoticeComment")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseResource @ApiResponseSignin
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
    @Operation(summary = "분양공고 댓글 수정", description = "분양공고 댓글의 내용을 수정합니다.", operationId = "updateNoticeCommentV1")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseResource @ApiResponseAuthority
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
    @Operation(summary = "분양공고 댓글 삭제", description = "분양공고 댓글을 삭제합니다.", operationId = "deleteNoticeComment")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseResource @ApiResponseAuthority
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
