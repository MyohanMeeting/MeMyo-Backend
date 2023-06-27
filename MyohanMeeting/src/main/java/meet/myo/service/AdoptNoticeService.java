package meet.myo.service;

import lombok.RequiredArgsConstructor;
import meet.myo.domain.Member;
import meet.myo.domain.Upload;
import meet.myo.domain.adopt.notice.*;
import meet.myo.domain.adopt.notice.cat.Cat;
import meet.myo.domain.adopt.notice.cat.Neutered;
import meet.myo.domain.adopt.notice.cat.Registered;
import meet.myo.domain.adopt.notice.cat.Sex;
import meet.myo.dto.request.adopt.*;
import meet.myo.dto.response.adopt.AdoptNoticeCommentResponseDto;
import meet.myo.dto.response.adopt.AdoptNoticeResponseDto;
import meet.myo.exception.NotFoundException;
import meet.myo.dto.response.adopt.AdoptNoticeSummaryResponseDto;
import meet.myo.repository.AdoptNoticeRepoImpl;

import meet.myo.repository.AdoptNoticeRepository;
import meet.myo.repository.MemberRepository;
import meet.myo.search.AdoptNoticeSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AdoptNoticeService {

    private final AdoptNoticeRepository adoptNoticeRepository;
    private final MemberRepository memberRepository;

    /**
     * 공고목록 전체 조회
     */
    @Transactional(readOnly = true)
    public List<AdoptNoticeSummaryResponseDto> getAdoptNoticeList(Pageable pageable, AdoptNoticeSearch search) {

        Page<AdoptNotice> adoptNotices = adoptNoticeRepository.searchAdoptNotices(pageable, search);
        return adoptNotices.getContent().stream()
                .map(notice -> {
                    AdoptNoticeSummaryResponseDto responseDto = new AdoptNoticeSummaryResponseDto();
                    responseDto.setNoticeId(notice.getId());
                    responseDto.setNoticeTitle(notice.getTitle());
                    responseDto.setNoticeStatus(notice.getStatus().toString());
                    responseDto.setThumbnail(notice.getThumbnail());
                    responseDto.setAuthorName(notice.getMember().getName());
                    responseDto.setCatName(notice.getCat().getName());
                    responseDto.setCatSpecies(notice.getCat().getSpecies());
                    responseDto.setShelterCity(notice.getShelter().getCity().toString());
                    return responseDto;
                })
                .collect(Collectors.toList());

        return List.of(AdoptNoticeSummaryResponseDto.fromEntity());

    }

    /**
     * 특정 회원의 공고목록 전체 조회
     */
    @Transactional(readOnly = true)
    public List<AdoptNoticeSummaryResponseDto> getMyAdoptNoticeList(Long memberId, Pageable pageable, String ordered) {

        Page<AdoptNotice> adoptNotices = adoptNoticeRepository.searchAdoptNoticesByMemberId(memberId, pageable, ordered);
        return adoptNotices.getContent().stream()
                .map(AdoptNoticeSummaryResponseDto::fromEntity)
                .collect(Collectors.toList());

        return List.of(AdoptNoticeSummaryResponseDto.fromEntity());

    }

    /**
     * 단일 조회
     */
    @Transactional(readOnly = true)
    public AdoptNoticeResponseDto getAdoptNotice(Long noticeId) {
        AdoptNotice adoptNotice = adoptNoticeRepository.findById(noticeId)
                .orElseThrow(() -> new NotFoundException("해당하는 입양공고가 존재하지 않습니다."));
        return AdoptNoticeResponseDto.fromEntity(adoptNotice);
    }
    /**
     * 작성
     */
    public Long createAdoptNotice(Long memberId, AdoptNoticeRequestDto dto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원이 존재하지 않습니다."));

        AdoptNotice adoptNotice = AdoptNotice.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .member(member)
                .build();

        adoptNoticeRepository.save(adoptNotice);
        return adoptNotice.getId();
    }
    /**
     * 상태수정
     */
    public AdoptNoticeResponseDto updateAdoptNoticeStatus(Long memberId, Long noticeId, AdoptNoticeStatusUpdateRequestDto dto) {
        AdoptNotice adoptNotice = adoptNoticeRepository.findById(noticeId)
                .orElseThrow(() -> new NotFoundException("요청하신 공고가 없습니다"));

        if (!adoptNotice.getMember().getId().equals(memberId)) {
            throw new NotFoundException("수정할 권한이 없습니다.");
        }

        AdoptNoticeStatus newStatus = AdoptNoticeStatus.valueOf(dto.getStatus());
        adoptNotice.updateNoticeStatus(newStatus);

        return AdoptNoticeResponseDto.fromEntity(adoptNotice);
    }

    /**
     * 수정
     */
    public AdoptNoticeResponseDto updateAdoptNotice(Long memberId, Long noticeId, AdoptNoticeRequestDto dto) {
        AdoptNotice adoptNotice = adoptNoticeRepository.findById(noticeId)
                .orElseThrow(() -> new NotFoundException("요청하신 공고가 없습니다"));

        if (!adoptNotice.getMember().getId().equals(memberId)) {
            throw new NotFoundException("수정할 권한이 없습니다.");
        }

        adoptNotice.updateTitle(dto.getTitle());
        adoptNotice.updateContent(dto.getContent());
        adoptNotice.updateCat(dto.getCat()); // cat 작성해야함

        return AdoptNoticeResponseDto.fromEntity(adoptNotice);
    }

    /**
     * 삭제
     */
    public Long deleteAdoptNotice(Long memberId, Long noticeId) {
        AdoptNotice adoptNotice = adoptNoticeRepository.findById(noticeId)
                .orElseThrow(() -> new NotFoundException("Adopt notice not found"));

        if (!adoptNotice.getMember().getId().equals(memberId)) {
            throw new new NotFoundException("요청하신 공고가 없습니다");
        }
        
        adoptNoticeRepository.delete(adoptNotice);
        // soft Delete로 변경
        return noticeId;
    }

    /**
     * 댓글 작성
     */
        return 1L;
    public Long createAdoptNoticeComment(Long memberId, AdoptNoticeCommentRequestDto dto) {
    }

    /**
     * 특정 공고글에 달린 댓글 목록 조회
     */
    public List<AdoptNoticeCommentResponseDto> getAdoptNoticeCommentList(Long noticeId) {
        return List.of(AdoptNoticeCommentResponseDto.fromEntity());
    }


        return AdoptNoticeCommentResponseDto.fromEntity();
    public AdoptNoticeCommentResponseDto updateAdoptNoticeComment(Long memberId, Long commentId, AdoptNoticeCommentRequestDto dto) {
    }

    public Long deleteAdoptNoticeComment(Long memberId, Long noticeCommentId) {
        return 1L;
    }
}
