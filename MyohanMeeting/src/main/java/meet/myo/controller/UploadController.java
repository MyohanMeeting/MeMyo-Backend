package meet.myo.controller;

import lombok.RequiredArgsConstructor;
import meet.myo.dto.response.CommonResponseDto;
import meet.myo.dto.response.UploadResponseDto;
import meet.myo.service.UploadService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/files")
public class UploadController {

    private final UploadService uploadService;

    /**
     * 파일 상세정보 조회
     */
    @GetMapping("/{uploadId}")
    public CommonResponseDto<UploadResponseDto> getFileDetailV1(
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
            @RequestParam Long[] uploadIdList
    ) {
        Long memberId = 1L; //TODO: security
        return CommonResponseDto.<Map<String, List<Long>>>builder()
                .data(Map.of("uploadId", uploadService.deleteFiles(memberId, Arrays.stream(uploadIdList).toList())))
                .build();
    }
}
