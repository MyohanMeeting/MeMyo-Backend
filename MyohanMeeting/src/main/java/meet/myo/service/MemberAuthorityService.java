package meet.myo.service;

import lombok.RequiredArgsConstructor;
import meet.myo.dto.response.MemberAuthorityResponseDto;
import meet.myo.repository.MemberAuthorityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberAuthorityService {

    private final MemberAuthorityRepository memberAuthorityRepository;

    /**
     * 권한 추가
     */
    public MemberAuthorityResponseDto addAuthority(Long memberId, List<String> authorityStrings) {
        return MemberAuthorityResponseDto.fromEntity();
    }

    /**
     * 권한 삭제
     */
    public MemberAuthorityResponseDto removeAuthority(Long memberId, List<String> authorityStrings) {
        return MemberAuthorityResponseDto.fromEntity();
    }
}
