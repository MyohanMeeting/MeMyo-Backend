package meet.myo.service;

import lombok.RequiredArgsConstructor;
import meet.myo.domain.Member;
import meet.myo.domain.Upload;
import meet.myo.domain.adopt.notice.*;
import meet.myo.domain.adopt.notice.cat.*;
import meet.myo.dto.request.adopt.*;
import meet.myo.dto.response.adopt.AdoptNoticeCommentResponseDto;
import meet.myo.dto.response.adopt.AdoptNoticeResponseDto;
import meet.myo.exception.NotFoundException;
import meet.myo.dto.response.adopt.AdoptNoticeSummaryResponseDto;

import meet.myo.repository.*;
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
    private final CatPictureRepository catPictureRepository;

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
    public Long createAdoptNotice(Long memberId, AdoptNoticeCreateRequestDto dto) {
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("회원이 존재하지 않습니다."));

        Cat cat = Cat.builder()
                .name(dto.getCat().getName())
                .age(dto.getCat().getAge())
                .registered(Registered.valueOf(dto.getCat().getRegistered()))
                .registNumber(dto.getCat().getRegistNumber())
                .species(dto.getCat().getSpecies())
                .sex(Sex.valueOf(dto.getCat().getSex()))
                .weight(dto.getCat().getWeight())
                .neutered(Neutered.valueOf(dto.getCat().getNeutered()))
                .healthStatus(dto.getCat().getHealthStatus())
                .personality(dto.getCat().getPersonality())
                .foundedPlace(dto.getCat().getFoundedPlace())
                .foundedAt(dto.getCat().getFoundedAt())
                .build();

        Shelter shelter = Shelter.builder()
                .name(dto.getShelter().getName())
                .city(City.valueOf(dto.getShelter().getCity()))
                .address(dto.getShelter().getAddress())
                .phoneNumber(dto.getShelter().getPhoneNumber())
                .build();

        Upload thumbnail = uploadRepository.findByIdAndDeletedAtNull(dto.getThumbnailId()).orElseThrow(NotFoundException::new);

        AdoptNotice adoptNotice = AdoptNotice.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .member(member)
                .cat(cat)
                .shelter(shelter)
                .thumbnail(thumbnail)
                .build();

        adoptNoticeRepository.save(adoptNotice);

        List<CatPicture> pictures = uploadRepository.findByIdInAndDeletedAtNull(dto.getCatPictures()).stream()
                .map(p -> CatPicture.createCatPicture(adoptNotice, p))
                .toList();
        catPictureRepository.saveAll(pictures);

        return adoptNotice.getId();
    }

    /**
     * 상태수정
     */
    public AdoptNoticeResponseDto updateAdoptNoticeStatus(Long memberId, Long noticeId, String status) {
        AdoptNotice adoptNotice = adoptNoticeRepository.findByIdAndDeletedAtNull(noticeId)
                .orElseThrow(() -> new NotFoundException("요청하신 공고가 없습니다"));

        if (!adoptNotice.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("수정할 권한이 없습니다.");
        }

        AdoptNoticeStatus newStatus = AdoptNoticeStatus.valueOf(status);
        adoptNotice.updateNoticeStatus(newStatus);

        return AdoptNoticeResponseDto.fromEntity(adoptNotice);
    }

    /**
     * 수정
     */
    public AdoptNoticeResponseDto updateAdoptNotice(Long memberId, Long noticeId, AdoptNoticeUpdateRequestDto dto) {
        AdoptNotice adoptNotice = adoptNoticeRepository.findByIdAndDeletedAtNull(noticeId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 공고가 없습니다."));

        if (!adoptNotice.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("수정할 권한이 없습니다.");
        }


        // 고양이 정보 수정
        if (dto.getCat().isPresent()) {
            AdoptNoticeUpdateRequestDto.Cat catDto = dto.getCat().get();
            Cat cat = adoptNotice.getCat();

            if (catDto.getName().isPresent()) { cat.updateName(catDto.getName().get()); }
            if (catDto.getAge().isPresent()) { cat.updateAge(catDto.getAge().get()); }
            if (catDto.getRegistered().isPresent() && catDto.getRegistered().get() != null) {
                cat.updateRegistered(Registered.valueOf(catDto.getRegistered().get()));
            }
            if (catDto.getRegistNumber().isPresent()) { cat.updateRegistNumber(catDto.getRegistNumber().get()); }
            if (catDto.getSpecies().isPresent()) { cat.updateSpecies(catDto.getSpecies().get()); }
            if (catDto.getSex().isPresent() && catDto.getSex().get() != null) {
                cat.updateSex(Sex.valueOf(catDto.getSex().get()));
            }
            if (catDto.getWeight().isPresent()) { cat.updateWeight(catDto.getWeight().get()); }
            if (catDto.getNeutered().isPresent() && catDto.getNeutered().get() != null) {
                cat.updateNeutered(Neutered.valueOf(catDto.getNeutered().get()));
            }
            if (catDto.getHealthStatus().isPresent()) { cat.updateHealthStatus(catDto.getHealthStatus().get()); }
            if (catDto.getPersonality().isPresent()) { cat.updatePersonality(catDto.getPersonality().get()); }
            if (catDto.getFoundedPlace().isPresent()) { cat.updateFoundedPlace(catDto.getFoundedPlace().get()); }
            if (catDto.getFoundedAt().isPresent()) { cat.updateFoundedAt(catDto.getFoundedAt().get()); }

        }

        // 보호소 정보 수정
        if (dto.getShelter().isPresent()) {
            AdoptNoticeUpdateRequestDto.Shelter shelterDto = dto.getShelter().get();
            Shelter shelter = adoptNotice.getShelter();

            if (shelterDto.getName().isPresent()) { shelter.updateName(shelterDto.getName().get()); }
            if (shelterDto.getCity().isPresent() && shelterDto.getCity().get() != null) {
                shelter.updateCity(City.valueOf(shelterDto.getCity().get()));
            }
            if (shelterDto.getAddress().isPresent()) { shelter.updateAddress(shelterDto.getAddress().get()); }
            if (shelterDto.getPhoneNumber().isPresent()) { shelter.updatePhoneNumber(shelterDto.getPhoneNumber().get()); }
        }

        // 썸네일 수정
        if (dto.getThumbnailId().isPresent()) {
            Upload thumbnail = uploadRepository.findByIdAndDeletedAtNull(dto.getThumbnailId().get()).orElseThrow(NotFoundException::new);
            adoptNotice.updateThumbnail(thumbnail);
        }

        // 제목, 내용 수정
        if (dto.getTitle().isPresent()) { adoptNotice.updateTitle(dto.getTitle().get()); }
        if (dto.getContent().isPresent()) { adoptNotice.updateContent(dto.getContent().get()); }

        // 사진 수정
        if (dto.getCatPictures().isPresent()) {
            List<Long> updateIds = dto.getCatPictures().get();
            List<CatPicture> oldCatPictures = catPictureRepository.findByNoticeIdInAndDeletedAtNull(adoptNotice.getId());

            // old 컬렉션에는 있으나 new 컬렉션에는 없는 사진 삭제
            // TODO: doDelete() 여기서 처리할지 배치로 처리할지 파일 삭제정책 결정
            oldCatPictures.stream()
                    .filter(p -> !updateIds.contains(p.getUpload().getId()))
                    .forEach(catPictureRepository::delete);

            // new 컬렉션에만 있는 사진 추가
            List<Long> newIds = updateIds.stream()
                    .filter(u -> !oldCatPictures.stream().map(o -> o.getUpload().getId()).toList().contains(u))
                    .toList();
            // TODO: 유효하지 않은 id가 입력되었을 때 검증하는 방법?
            List<CatPicture> newCatPictures = uploadRepository.findByIdInAndDeletedAtNull(newIds).stream()
                    .map(u -> CatPicture.createCatPicture(adoptNotice, u))
                    .toList();
            catPictureRepository.saveAll(newCatPictures);
        }

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
    public Long createAdoptNoticeComment(Long memberId, AdoptNoticeCommentCreateRequestDto dto) {
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


    public AdoptNoticeCommentResponseDto updateAdoptNoticeComment(Long memberId, Long commentId, AdoptNoticeCommentUpdateRequestDto dto) {
        AdoptNoticeComment comment = adoptNoticeCommentRepository.findByIdAndDeletedAtNull(commentId).orElseThrow(NotFoundException::new);
        if (!comment.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("ACCESS DENIED");
        }
        comment.updateContent(dto.getContent());
        return AdoptNoticeCommentResponseDto.fromEntity(comment);
    }

    public Long deleteAdoptNoticeComment(Long memberId, Long noticeCommentId) {
        AdoptNoticeComment comment = adoptNoticeCommentRepository.findByIdAndDeletedAtNull(noticeCommentId).orElseThrow(NotFoundException::new);
        if (!comment.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("ACCESS DENIED");
        }
        comment.delete();
        comment.getAdoptNotice().removeComment();
        return comment.getId();
    }
}
