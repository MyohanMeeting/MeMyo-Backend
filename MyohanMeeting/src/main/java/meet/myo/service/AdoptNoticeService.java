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

import meet.myo.repository.AdoptNoticeCommentRepository;
import meet.myo.repository.AdoptNoticeRepository;
import meet.myo.repository.MemberRepository;
import meet.myo.repository.UploadRepository;
import meet.myo.search.AdoptNoticeSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AdoptNoticeService {

    private final AdoptNoticeRepository adoptNoticeRepository;
    private final AdoptNoticeCommentRepository adoptNoticeCommentRepository;
    private final MemberRepository memberRepository;
    private final UploadRepository uploadRepository;

    /**
     * 공고목록 전체 조회
     */
    @Transactional(readOnly = true)
    public List<AdoptNoticeSummaryResponseDto> getAdoptNoticeList(Pageable pageable, AdoptNoticeSearch search) {

        Page<AdoptNotice> adoptNotices = adoptNoticeRepository.findByDeletedAtNull(pageable);
        return adoptNotices.getContent().stream()
                .map(AdoptNoticeSummaryResponseDto::fromEntity)
                .collect(Collectors.toList());

    }

    /**
     * 특정 회원의 공고목록 전체 조회
     */
    @Transactional(readOnly = true)
    public List<AdoptNoticeSummaryResponseDto> getMyAdoptNoticeList(Long memberId, Pageable pageable, String ordered) {

        Page<AdoptNotice> adoptNotices = adoptNoticeRepository.findByMemberIdAndDeletedAtNull(pageable, memberId);
        return adoptNotices.getContent().stream()
                .map(AdoptNoticeSummaryResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 단일 조회
     */
    @Transactional(readOnly = true)
    public AdoptNoticeResponseDto getAdoptNotice(Long noticeId) {
        AdoptNotice adoptNotice = adoptNoticeRepository.findByIdAndDeletedAtNull(noticeId)
                .orElseThrow(() -> new NotFoundException("해당하는 입양공고가 존재하지 않습니다."));
        return AdoptNoticeResponseDto.fromEntity(adoptNotice);
    }

    /**
     * 작성
     */
    public Long createAdoptNotice(Long memberId, AdoptNoticeRequestDto dto) {
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("회원이 존재하지 않습니다."));

        AdoptNotice adoptNotice = AdoptNotice.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .member(member)
                .build();

        Cat cat = Cat.builder()
                .name(dto.getCat().getName())
                .age(dto.getCat().getAge())
                .registered(dto.getCat().getRegistered() != null ? Registered.valueOf(dto.getCat().getRegistered()) : null)
                .registNumber(dto.getCat().getRegistNumber())
                .species(dto.getCat().getSpecies())
                .sex(dto.getCat().getSex() != null ? Sex.valueOf(dto.getCat().getSex()) : null)
                .weight(dto.getCat().getWeight())
                .neutered(dto.getCat().getNeutered() != null ? Neutered.valueOf(dto.getCat().getNeutered()) : null)
                .healthStatus(dto.getCat().getHealthStatus())
                .personality(dto.getCat().getPersonality())
                .foundedPlace(dto.getCat().getFoundedPlace())
                .foundedAt(dto.getCat().getFoundedAt())
                .build();

        Shelter shelter = Shelter.builder()
                .name(dto.getShelter().getName())
                .city(dto.getShelter().getCity() != null ? City.valueOf(dto.getShelter().getCity()) : null)
                .address(dto.getShelter().getAddress())
                .phoneNumber(dto.getShelter().getPhoneNumber())
                .build();

        Upload thumbnail = uploadRepository.findByIdAndDeletedAtNull(dto.getThumbnailId()).orElseThrow(NotFoundException::new);

        adoptNoticeRepository.save(adoptNotice);
        return adoptNotice.getId();
    }

    /**
     * 상태수정
     */
    public AdoptNoticeResponseDto updateAdoptNoticeStatus(Long memberId, Long noticeId, AdoptNoticeStatusUpdateRequestDto dto) {
        AdoptNotice adoptNotice = adoptNoticeRepository.findByIdAndDeletedAtNull(noticeId)
                .orElseThrow(() -> new NotFoundException("요청하신 공고가 없습니다"));

        if (!adoptNotice.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("수정할 권한이 없습니다.");
        }

        AdoptNoticeStatus newStatus = AdoptNoticeStatus.valueOf(dto.getStatus());
        adoptNotice.updateNoticeStatus(newStatus);

        return AdoptNoticeResponseDto.fromEntity(adoptNotice);
    }

    /**
     * 수정
     */
    public AdoptNoticeResponseDto updateAdoptNotice(Long memberId, Long noticeId, AdoptNoticeRequestDto dto) {
        AdoptNotice adoptNotice = adoptNoticeRepository.findByIdAndDeletedAtNull(noticeId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 공고가 없습니다."));

        if (!adoptNotice.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("수정할 권한이 없습니다.");
        }

        //TODO: PATCH 처리
        Cat cat = Cat.builder()
                .name(dto.getCat().getName())
                .age(dto.getCat().getAge())
                .registered(dto.getCat().getRegistered() != null ? Registered.valueOf(dto.getCat().getRegistered()) : null)
                .registNumber(dto.getCat().getRegistNumber())
                .species(dto.getCat().getSpecies())
                .sex(dto.getCat().getSex() != null ? Sex.valueOf(dto.getCat().getSex()) : null)
                .weight(dto.getCat().getWeight())
                .neutered(dto.getCat().getNeutered() != null ? Neutered.valueOf(dto.getCat().getNeutered()) : null)
                .healthStatus(dto.getCat().getHealthStatus())
                .personality(dto.getCat().getPersonality())
                .foundedPlace(dto.getCat().getFoundedPlace())
                .foundedAt(dto.getCat().getFoundedAt())
                .build();

        Shelter shelter = Shelter.builder()
                .name(dto.getShelter().getName())
                .city(dto.getShelter().getCity() != null ? City.valueOf(dto.getShelter().getCity()) : null)
                .address(dto.getShelter().getAddress())
                .phoneNumber(dto.getShelter().getPhoneNumber())
                .build();

        Upload thumbnail = uploadRepository.findById(dto.getThumbnailId()).orElseThrow(NotFoundException::new);

        adoptNotice.updateTitle(dto.getTitle());
        adoptNotice.updateContent(dto.getContent());
        adoptNotice.updateThumbnail(thumbnail);
        // catPictuers collection update 코드 필요

        return AdoptNoticeResponseDto.fromEntity(adoptNotice);
    }

    /**
     * 삭제
     */
    public Long deleteAdoptNotice(Long memberId, Long noticeId) {
        AdoptNotice adoptNotice = adoptNoticeRepository.findByIdAndDeletedAtNull(noticeId)
                .orElseThrow(() -> new NotFoundException("Adopt notice not found"));

        if (!adoptNotice.getMember().getId().equals(memberId)) {
            throw new NotFoundException("요청하신 공고가 없습니다");
        }

        adoptNotice.delete();
        return adoptNotice.getId();
    }

    /**
     * 댓글 작성
     */
    public Long createAdoptNoticeComment(Long memberId, AdoptNoticeCommentRequestDto dto) {
        AdoptNotice notice = adoptNoticeRepository.findByIdAndDeletedAtNull(dto.getNoticeId()).orElseThrow(NotFoundException::new);
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId).orElseThrow(NotFoundException::new);
        AdoptNoticeComment comment = AdoptNoticeComment.builder()
                .adoptNotice(notice)
                .member(member)
                .content(dto.getContent())
                .build();
        adoptNoticeCommentRepository.save(comment);
        notice.addComment();
        return comment.getId();
    }

    /**
     * 특정 공고글에 달린 댓글 목록 조회
     */
    public List<AdoptNoticeCommentResponseDto> getAdoptNoticeCommentList(Long noticeId) {
        return adoptNoticeCommentRepository.findByAdoptNoticeIdAndDeletedAtNull(noticeId)
                .stream().map(AdoptNoticeCommentResponseDto::fromEntity)
                .toList();
    }


    public AdoptNoticeCommentResponseDto updateAdoptNoticeComment(Long memberId, Long commentId, AdoptNoticeCommentRequestDto dto) {
        AdoptNoticeComment comment = adoptNoticeCommentRepository.findByIdAndDeletedAtNull(commentId).orElseThrow(NotFoundException::new);
        if (comment.getMember().getId() != memberId) {
            throw new AccessDeniedException("ACCESS DENIED");
        }
        comment.updateContent(dto.getContent());
        return AdoptNoticeCommentResponseDto.fromEntity(comment);
    }

    public Long deleteAdoptNoticeComment(Long memberId, Long noticeCommentId) {
        AdoptNoticeComment comment = adoptNoticeCommentRepository.findByIdAndDeletedAtNull(noticeCommentId).orElseThrow(NotFoundException::new);
        if (comment.getMember().getId() != memberId) {
            throw new AccessDeniedException("ACCESS DENIED");
        }
        comment.delete();
        comment.getAdoptNotice().removeComment();
        return comment.getId();
    }
}
