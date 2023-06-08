package meet.myo.controller;

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

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/adoption/applications")
public class AdoptApplicationController {

    private final AdoptApplicationService adoptApplicationService;

    /**
     * 특정 분양신청 상세조회
     */
    @GetMapping("/{applicationId}")
    public CommonResponseDto<AdoptApplicationResponseDto> getApplicationV1(
            @PathVariable(name = "applicationId") Long applicationId
    ) {
        return CommonResponseDto.<AdoptApplicationResponseDto>builder()
                .data(adoptApplicationService.getAdoptApplication(applicationId))
                .build();
    }

    /**
     * 내가 한 분양신청 목록조회
     */
    @GetMapping("/my")
    public CommonResponseDto<List<AdoptApplicationResponseDto>> getMyApplicationListV1(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "100") int size,
            @RequestParam(value = "ordered", required = false) String ordered
    ) {
        Long memberId = 1L; // TODO: security
        Pageable pageable = PageRequest.of(page, size);
        return CommonResponseDto.<List<AdoptApplicationResponseDto>>builder()
               .data(adoptApplicationService.getMyAdoptApplicationList(memberId, pageable, ordered))
               .build();
    }

    /**
     * 특정 공고에 달린 신청 모아보기(공고 작성자만 조회가능)
     */
    @GetMapping("/byNotice/{noticeId}")
    public CommonResponseDto<List<AdoptApplicationResponseDto>> getApplicationListByNoticeV1(
            @PathVariable(name = "noticeId") Long noticeId,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "100") int size,
            @RequestParam(value = "ordered", required = false) String ordered
    ) {
        Long memberId = 1L; // TODO: security
        Pageable pageable = PageRequest.of(page, size);
        return CommonResponseDto.<List<AdoptApplicationResponseDto>>builder()
                .data(adoptApplicationService.getAdoptApplicationListByNotice(memberId, noticeId, pageable, ordered))
                .build();
    }

    /**
     * 분양신청 작성
     */
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
    @PatchMapping("/{applicationId}")
    public CommonResponseDto<AdoptApplicationResponseDto> updateApplicationV1(
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
    @DeleteMapping("/{applicationId}")
    public CommonResponseDto<Map<String, Long>> deleteApplicationV1(
            @PathVariable(name = "applicationId") Long applicationId
    ) {
        Long memberId = 1L; //TODO: security
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("applicationid", adoptApplicationService.deleteAdoptApplication(memberId, applicationId)))
                .build();
    }

}
