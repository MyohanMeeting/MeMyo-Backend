package meet.myo.service;

import lombok.RequiredArgsConstructor;
import meet.myo.dto.request.AdoptNoticeCreateRequestDto;
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
     * 내가 쓴 공고목록 조회
     */
    @Transactional(readOnly = true)
    public List<AdoptNoticeResponseDto> getMyAdoptNoticeList(Pageable pageable, Long memberId) {
        return List.of(AdoptNoticeResponseDto.fromEntity());
    }

    /**
     * 단일 조회
     */
    @Transactional(readOnly = true)
    public AdoptNoticeResponseDto getAdoptNotice(Long id) {
        return AdoptNoticeResponseDto.fromEntity();
    }

    /**
     * 작성
     */
    public Long createAdoptNotice(AdoptNoticeCreateRequestDto dto) {
        return 1L;
    }

    /**
     * 수정
     */
    //TODO: 글 상태만 변경하는 엔드포인트를 따로 만들어야 할까요?
    public AdoptNoticeResponseDto updateAdoptNotice(AdoptNoticeUpdateRequestDto dto) {
        return AdoptNoticeResponseDto.fromEntity();
    }

    /**
     * 삭제
     */
    public Long deleteAdoptNotice(Long id) {
        return 1L;
    }
}
