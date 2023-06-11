package meet.myo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import meet.myo.dto.request.DeleteFilesRequestDto;
import meet.myo.dto.response.CommonResponseDto;
import meet.myo.dto.response.UploadResponseDto;
import meet.myo.service.UploadService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Tag(name = "File", description = "파일 업로드 관련 기능")
@SecurityRequirement(name = "JWT")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/files")
public class UploadController {

    private final UploadService uploadService;

    /**
     * 파일 상세정보 조회
     */
    @Operation(summary = "파일 상세정보 조회", description = "업로드한 파일의 상세정보를 조회합니다.", operationId = "getFileDetail")
    @GetMapping("/{uploadId}")
    public CommonResponseDto<UploadResponseDto> getFileDetailV1(
            @Parameter(name = "noticeId", description = "삭제하고자 하는 공고의 id입니다.")
            @PathVariable(name = "uploadId") Long uploadId
    ) {
        Long memberId = 1L; //TODO: security
        return CommonResponseDto.<UploadResponseDto>builder()
                .data(uploadService.getFileDetail(memberId, uploadId))
                .build();
    }

    /**
     * 파일 업로드
     */
    @PostMapping("")
    public CommonResponseDto<Map<String, List<Long>>> uploadFilesV1(
            @Parameter(name = "noticeId", description = "삭제하고자 하는 공고의 id입니다.")
            @RequestParam MultipartFile[] files
    ) {
        Long memberId = 1L; //TODO: security
        return CommonResponseDto.<Map<String, List<Long>>>builder()
                .data(Map.of("uploadId", uploadService.uploadFiles(memberId, Arrays.stream(files).toList())))
                .build();
    }

    /**
     * 파일 삭제
     */
    @DeleteMapping("")
    public CommonResponseDto<Map<String, List<Long>>> deleteFilesV1(
            @Parameter(name = "noticeId", description = "삭제하고자 하는 공고의 id입니다.")
            @Validated @RequestBody final DeleteFilesRequestDto dto
    ) {
        Long memberId = 1L; //TODO: security
        return CommonResponseDto.<Map<String, List<Long>>>builder()
                .data(Map.of("uploadId", uploadService.deleteFiles(memberId, dto)))
                .build();
    }
}