package meet.myo.service;

import lombok.RequiredArgsConstructor;
import meet.myo.domain.Favorite;
import meet.myo.domain.Member;
import meet.myo.domain.cat.Cat;
import meet.myo.exception.NotFoundException;
import meet.myo.dto.request.CreateFavoriteRequestDto;
import meet.myo.dto.response.FavoriteResponseDto;
import meet.myo.repository.CatRepository;
import meet.myo.repository.FavoriteRepository;
import meet.myo.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final CatRepository catRepository;

    /*
     * 찜 목록 가져오기
     */
    public List<FavoriteResponseDto> getFavoriteList(Long memberId, Pageable pageable) {
        Page<Favorite> favoritePage = favoriteRepository.findByIdAndDeletedAtNull(memberId, pageable);

        List<FavoriteResponseDto> favoriteList = favoritePage.stream()
                .map(FavoriteResponseDto::fromEntity)
                .collect(Collectors.toList());

        return favoriteList;
    }

    /*
     * 찜하기
     */
    public Long createFavorite(Long memberId, CreateFavoriteRequestDto dto) {
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다."));

        Long catId = dto.getCatId();
        Cat cat = catRepository.findByIdAndDeletedAtNull(catId)
                .orElseThrow(() -> new NotFoundException("해당하는 고양이를 찾을 수 없습니다."));

        Favorite savedFavorite = favoriteRepository.save(Favorite.createFavorite(member, cat));
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
            throw new NotFoundException("찜 데이터가 존재하지 않습니다.");
        }
        favorite.delete();
        return favorite.getId();
    }


}
