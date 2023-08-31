package meet.myo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import meet.myo.config.SecurityUtil;
import meet.myo.dto.request.adopt.AdoptNoticeCommentCreateRequestDto;
import meet.myo.dto.request.adopt.AdoptNoticeCommentUpdateRequestDto;
import meet.myo.dto.response.adopt.AdoptNoticeCommentResponseDto;
import meet.myo.dto.response.CommonResponseDto;
import meet.myo.exception.NotAuthenticatedException;
import meet.myo.service.AdoptNoticeService;
import meet.myo.springdoc.annotations.ApiResponseAuthority;
import meet.myo.springdoc.annotations.ApiResponseCommon;
import meet.myo.springdoc.annotations.ApiResponseResource;
import meet.myo.springdoc.annotations.ApiResponseSignin;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "5. Adopt Notice Comment", description = "분양공고 댓글 관련 기능")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/adoption/notices")
@PreAuthorize("hasAnyRole('ROLE_USER')")
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
    @ApiResponse(responseCode = "200", description = "작성 성공", content = @Content(
            schema = @Schema(implementation = CommonResponseDto.class), examples = { @ExampleObject(value = """
{
  "status": "200 OK",
  "timestamp": "2023-06-10T09:19:08.550Z",
  "message": "SUCCESS",
  "data": {
    "noticeCommentId" : 1
  }
}
""")})) @ApiResponseCommon @ApiResponseResource @ApiResponseSignin
    @SecurityRequirement(name = "Authorization")
    @PostMapping("/comments")
    public CommonResponseDto<Map<String, Long>> createNoticeCommentV1(
            @Valid @RequestBody final AdoptNoticeCommentCreateRequestDto dto
    ) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID_ID"));
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("noticeId", adoptNoticeService.createAdoptNoticeComment(memberId, dto)))
                .build();
    }

    /**
     * 분양공고 댓글 수정
     */
    @Operation(summary = "분양공고 댓글 수정", description = "분양공고 댓글의 내용을 수정합니다.", operationId = "updateNoticeCommentV1")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseResource @ApiResponseAuthority
    @PutMapping("/comments/{noticeCommentId}")
    @SecurityRequirement(name = "Authorization")
    public CommonResponseDto<AdoptNoticeCommentResponseDto> updateNoticeCommentV1(
            @Parameter(name = "noticeCommentId", description = "수정하고자 하는 댓글의 id입니다.")
            @PathVariable(name = "noticeCommentId") Long noticeCommentId,

            @RequestBody final AdoptNoticeCommentUpdateRequestDto dto
    ) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID_ID"));
        return CommonResponseDto.<AdoptNoticeCommentResponseDto>builder()
                .data(adoptNoticeService.updateAdoptNoticeComment(memberId, noticeCommentId, dto))
                .build();
    }

    /**
     * 분양공고 댓글 삭제
     */
    @Operation(summary = "분양공고 댓글 삭제", description = "분양공고 댓글을 삭제합니다.", operationId = "deleteNoticeComment")
    @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(
            schema = @Schema(implementation = CommonResponseDto.class), examples = { @ExampleObject(value = """
{
  "status": "200 OK",
  "timestamp": "2023-06-10T09:19:08.550Z",
  "message": "SUCCESS",
  "data": {
    "noticeCommentId" : 1
  }
}
""")})) @ApiResponseCommon @ApiResponseResource @ApiResponseAuthority
    @SecurityRequirement(name = "Authorization")
    @DeleteMapping("/comments/{noticeCommentId}")
    public CommonResponseDto<Map<String, Long>> deleteNoticeCommentV1(
            @Parameter(name = "noticeCommentId", description = "삭제하고자 하는 댓글의 id입니다.")
            @PathVariable(name = "noticeCommentId") Long noticeCommentId
    ) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID_ID"));
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("noticeCommentId", adoptNoticeService.deleteAdoptNoticeComment(memberId, noticeCommentId)))
                .build();
    }
}
