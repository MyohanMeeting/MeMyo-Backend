package meet.myo.service;

import lombok.RequiredArgsConstructor;
import meet.myo.dto.request.AdoptNoticeCreateRequestDto;
import meet.myo.dto.request.AdoptNoticeStatusUpdateRequestDto;
import meet.myo.dto.response.AdoptNoticeResponseDto;
import meet.myo.dto.request.AdoptNoticeUpdateRequestDto;
import meet.myo.repository.AdoptNoticeRepoImpl;
import meet.myo.repository.AdoptNoticeRepository;
import meet.myo.search.AdoptNoticeSearch;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdoptNoticeService {

    private final AdoptNoticeRepository adoptNoticeRepository;

    private final AdoptNoticeRepoImpl adoptNoticeRepoImpl;

    /**
     * 공고목록 전체 조회
     */
    @Transactional(readOnly = true)
    public List<AdoptNoticeResponseDto> getAdoptNoticeList(Pageable pageable, AdoptNoticeSearch search) {
        return List.of(AdoptNoticeResponseDto.fromEntity());
    }

    /**
     * 특정 회원의 공고목록 전체 조회
     */
    @Transactional(readOnly = true)
    public List<AdoptNoticeResponseDto> getMyAdoptNoticeList(Long memberId, Pageable pageable, String ordered) {
        return List.of(AdoptNoticeResponseDto.fromEntity());
    }

    /**
     * 단일 조회
     */
    @Transactional(readOnly = true)
    public AdoptNoticeResponseDto getAdoptNotice(Long noticeId) {
        return AdoptNoticeResponseDto.fromEntity();
    }

    /**
     * 작성
     */
    public Long createAdoptNotice(Long memberId, AdoptNoticeCreateRequestDto dto) {
        return 1L;
    }

    /**
     * 상태 수정
     */
    public AdoptNoticeResponseDto updateAdoptNoticeStatus(Long memberId, Long noticeId, AdoptNoticeStatusUpdateRequestDto dto) {
        return AdoptNoticeResponseDto.fromEntity();
    }

    /**
     * 수정
     */
    public AdoptNoticeResponseDto updateAdoptNotice(Long memberId, Long noticeId, AdoptNoticeUpdateRequestDto dto) {
        return AdoptNoticeResponseDto.fromEntity();
    }

    /**
     * 삭제
     */
    public Long deleteAdoptNotice(Long memberId, Long noticeId) {
        return 1L;
    }
}
