package meet.myo.service;

import lombok.RequiredArgsConstructor;
import meet.myo.dto.request.adopt.AdoptApplicationCreateRequestDto;
import meet.myo.dto.request.adopt.AdoptApplicationUpdateRequestDto;
import meet.myo.dto.response.adopt.AdoptApplicationResponseDto;
import meet.myo.repository.AdoptNoticeRepoImpl;
import meet.myo.repository.AdoptNoticeRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdoptApplicationService {
    private final AdoptNoticeRepository adoptNoticeRepository;

    private final AdoptNoticeRepoImpl adoptNoticeRepoImpl;

    /**
     * 특정 공고에 달린 분양신청 목록 조회
     */
    @Transactional(readOnly = true)
    public List<AdoptApplicationResponseDto> getAdoptApplicationListByNotice(Long memberId, Long noticeId, Pageable pageable, String ordered) {
        return List.of(AdoptApplicationResponseDto.fromEntity());
    }

    /**
     * 내가 작성한 분양신청 목록 조회
     */
    @Transactional(readOnly = true)
    public List<AdoptApplicationResponseDto> getMyAdoptApplicationList(Long memberId, Pageable pageable, String ordered) {
        return List.of(AdoptApplicationResponseDto.fromEntity());
    }

    /**
     * 단일 조회
     */
    @Transactional(readOnly = true)
    public AdoptApplicationResponseDto getAdoptApplication(Long applicationId) {
        return AdoptApplicationResponseDto.fromEntity();
    }

    /**
     * 작성
     */
    public Long createAdoptApplication(Long memberId, AdoptApplicationCreateRequestDto dto) {
        return 1L;
    }

    /**
     * 수정
     */
    public AdoptApplicationResponseDto updateAdoptApplication(Long memberId, Long applicationId, AdoptApplicationUpdateRequestDto dto) {
        return AdoptApplicationResponseDto.fromEntity();
    }

    /**
     * 삭제
     */
    public Long deleteAdoptApplication(Long memberId, Long applicationId) {
        return 1L;
    }
}
