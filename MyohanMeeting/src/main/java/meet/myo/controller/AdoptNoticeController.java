package meet.myo.controller;

import lombok.RequiredArgsConstructor;
import meet.myo.dto.request.AdoptNoticeCreateRequestDto;
import meet.myo.dto.request.AdoptNoticeStatusUpdateRequestDto;
import meet.myo.dto.request.AdoptNoticeUpdateRequestDto;
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
    @GetMapping("")
    public CommonResponseDto<List<AdoptNoticeResponseDto>> getNoticeListV1(
            // 페이징
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "100") int size,

            // 공고 내 정보
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "authorName", required = false) String authorName,
            @RequestParam(value = "noticeStatus", required = false) String noticeStatus,

            // 고양이 정보
            @RequestParam(value = "catName", required = false) String catName,
            @RequestParam(value = "catSpecies", required = false) String catSpecies,
            @RequestParam(value = "catAge", required = false) String catAge,
            @RequestParam(value = "catSex", required = false) String catSex,
            @RequestParam(value = "neutered", required = false) String neutered,
            @RequestParam(value = "healthStatus", required = false) String healthStatus,
            @RequestParam(value = "personality", required = false) String personality,
            @RequestParam(value = "foundedPlace", required = false) String foundedPlace,
            @RequestParam(value = "foundedAt", required = false) String foundedAt,

            // 보호소 정보
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "shelterName", required = false) String shelterName,

            // 정렬 기준
            @RequestParam(value = "ordered", required = false) String ordered
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
                .healthStatus(healthStatus)
                .personality(personality)
                .foundedPlace(foundedPlace)
                .foundedAt(foundedAt)

                // 보호소 정보
                .city(city)
                .shelterName(shelterName)

                // 정렬
                .ordered(ordered)
                .build();

        return CommonResponseDto.<List<AdoptNoticeResponseDto>>builder()
                .data(adoptNoticeService.getAdoptNoticeList(pageable, search))
                .build();
    }

    /**
     * 내가 올린 분양공고 목록조회
     */
    @GetMapping("/my")
    public CommonResponseDto<List<AdoptNoticeResponseDto>> getMyNoticeListV1(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "100") int size,
            @RequestParam(value = "ordered", required = false) String ordered
    ) {
        Long memberId = 1L; // TODO: security
        Pageable pageable = PageRequest.of(page, size);
        return CommonResponseDto.<List<AdoptNoticeResponseDto>>builder()
                .data(adoptNoticeService.getMyAdoptNoticeList(memberId, pageable, ordered))
                .build();
    }

    /**
     * 분양공고 상세조회
     */
    @GetMapping("/{noticeId}")
    public CommonResponseDto<AdoptNoticeResponseDto> getNoticeV1(@PathVariable(name = "noticeId") Long noticeId) {
        return CommonResponseDto.<AdoptNoticeResponseDto>builder()
                .data(adoptNoticeService.getAdoptNotice(noticeId))
                .build();
    }

    /**
     * 분양공고 작성
     */
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
    @PatchMapping("/status/{noticeId}")
    public CommonResponseDto<AdoptNoticeResponseDto> updateNoticeStatusV1(
            @PathVariable(name = "noticeId") Long noticeId, @Validated @RequestBody final AdoptNoticeStatusUpdateRequestDto dto
    ) {
        Long memberId = 1L; //TODO: security
        return CommonResponseDto.<AdoptNoticeResponseDto>builder()
                .data(adoptNoticeService.updateAdoptNoticeStatus(memberId, noticeId, dto))
                .build();
    }

    /**
     * 분양공고 수정
     */
    @PatchMapping("/{noticeId}")
    public CommonResponseDto<AdoptNoticeResponseDto> updateNoticeV1(
            @PathVariable(name = "noticeId") Long noticeId, @Validated @RequestBody final AdoptNoticeUpdateRequestDto dto
    ) {
        Long memberId = 1L; //TODO: security
        return CommonResponseDto.<AdoptNoticeResponseDto>builder()
                .data(adoptNoticeService.updateAdoptNotice(memberId, noticeId, dto))
                .build();
    }

    /**
     * 분양공고 삭제
     */
    @DeleteMapping("/{noticeId}")
    public CommonResponseDto<Map<String, Long>> deleteNoticeV1(@PathVariable(name = "noticeId") Long noticeId) {
        Long memberId = 1L; //TODO: security
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("noticeId", adoptNoticeService.deleteAdoptNotice(memberId, noticeId)))
                .build();
    }
}
