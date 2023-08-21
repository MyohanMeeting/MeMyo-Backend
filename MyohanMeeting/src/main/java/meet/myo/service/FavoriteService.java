package meet.myo.service;

import lombok.RequiredArgsConstructor;
import meet.myo.domain.Favorite;
import meet.myo.domain.Member;
import meet.myo.domain.adopt.notice.AdoptNotice;
import meet.myo.exception.NotFoundException;
import meet.myo.dto.request.CreateFavoriteRequestDto;
import meet.myo.dto.response.FavoriteResponseDto;
import meet.myo.repository.AdoptNoticeRepository;
import meet.myo.repository.FavoriteRepository;
import meet.myo.repository.MemberRepository;
import org.springframework.dao.DuplicateKeyException;
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
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final AdoptNoticeRepository adoptNoticeRepository;

    /*
     * 찜 목록 가져오기
     */
    public List<FavoriteResponseDto> getFavoriteList(Long memberId, Pageable pageable) {
        Page<Favorite> favoritePage = favoriteRepository.findByMemberIdAndDeletedAtNull(memberId, pageable);

        List<FavoriteResponseDto> favoriteList = favoritePage.stream()
                .map(FavoriteResponseDto::fromEntity)
                .collect(Collectors.toList());

        return favoriteList;
    }

    /*
     * 찜하기
     */
    public Long createFavorite(Long memberId, Long noticeId) {

        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        AdoptNotice notice = adoptNoticeRepository.findByIdAndDeletedAtNull(noticeId)
                .orElseThrow(() -> new NotFoundException("공고를 찾을 수 없습니다."));

        if (favoriteRepository.findByMemberIdAndAdoptNoticeIdAndDeletedAtNull(member.getId(), notice.getId()).isPresent()) {
            throw new DuplicateKeyException("이미 등록한 최애친구입니다.");
        }

        Favorite savedFavorite = favoriteRepository.save(Favorite.createFavorite(member, notice));
        return savedFavorite.getId();
    }

    /*
     * 찜 삭제하기
     */
    public Long deleteFavorite(Long memberId, Long favoriteId) {
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다."));

        Favorite favorite = favoriteRepository.findByIdAndDeletedAtNull(favoriteId)
                .orElseThrow(() -> new NotFoundException("찜 데이터가 존재하지 않습니다."));

        // Favorite 엔티티가 해당 회원의 찜 데이터인지 확인
        if (!favorite.getMember().equals(member)) {
            throw new AccessDeniedException("ACCESS DENIED");
        }
        favorite.delete();
        return favorite.getId();
    }


}
