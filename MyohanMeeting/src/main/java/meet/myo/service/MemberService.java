package meet.myo.service;

import lombok.RequiredArgsConstructor;
import meet.myo.domain.*;
import meet.myo.domain.authority.MemberAuthority;
import meet.myo.dto.request.member.OauthDeleteRequestDto;
import meet.myo.dto.request.member.*;
import meet.myo.dto.response.member.EmailUpdateResponseDto;
import meet.myo.dto.response.member.MemberResponseDto;
import meet.myo.dto.response.member.MemberUpdateResponseDto;
import meet.myo.dto.response.member.OauthRegisterResponseDto;
import meet.myo.exception.NotFoundException;
import meet.myo.repository.*;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberAuthorityRepository memberAuthorityRepository;
    private final UploadRepository uploadRepository;


    /**
     * 회원정보 조회
     */
    @Transactional(readOnly = true)
    public MemberResponseDto getMemberById(Long id) {
        return MemberResponseDto.fromEntity(
                memberRepository.findByIdAndDeletedAtNull(id).orElseThrow(
                        () -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다.")));
    }


    /**
     * 이메일 수정
     */
    @Transactional
    public EmailUpdateResponseDto updateEmail(Long memberId, EmailUpdateRequestDto dto) {
        String newEmail = dto.getNewEmail();
        validateEmailDuplication(newEmail); // 이메일 중복체크 추가

        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다."));
        member.updateEmail(newEmail);

        return EmailUpdateResponseDto.fromEntity(member);
    }


    /**
     * Oauth 연결
     */
    public OauthRegisterResponseDto registerOauth(Long memberId, OauthRegisterRequestDto dto) {
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다."));

        if (member.getOauth() != null) {
            throw new DuplicateKeyException("이미 연결된 SNS가 존재합니다. 변경을 원하시면 삭제 후 다시 등록해 주세요.");
        }

        Oauth updatedOauth = Oauth.createOauth(OauthType.valueOf(dto.getOauthType()), dto.getOauthId()); //TODO: null check
        member.updateOauth(updatedOauth);

        return OauthRegisterResponseDto.fromEntity(member);
    }


    /**
     * Oauth 삭제
     */
    public void deleteOauth(Long memberId, OauthDeleteRequestDto dto) {
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다."));

        if (member.getPassword() == null) {
            throw new IllegalArgumentException("SNS 로그인 정보를 삭제하려면 먼저 비밀번호를 설정해야 합니다.");
        }

        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            throw new AccessDeniedException("비밀번호가 일치하지 않습니다.");
        }

        member.updateOauth(null);
    }


    /**
     * 비밀번호 수정
     */
    public void updatePassword(Long memberId, PasswordUpdateRequestDto dto) {

        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다."));

        String currentPassword = dto.getCurrentPassword();
        // 현재 설정된 비밀번호가 없다면 비밀번호를 검증하지 않음. 있다면 검증.
        if (member.getPassword() != null && !passwordEncoder.matches(currentPassword, member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String encodedPassword = passwordEncoder.encode(dto.getNewPassword());
        member.updatePassword(encodedPassword);
    }


    /**
     * 개인정보 수정
     */
    public MemberUpdateResponseDto updateMember(Long memberId, MemberUpdateRequestDto dto) {
        Member member = memberRepository.findMemberWithId(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다."));

        if (dto.getNickname().isPresent()) { member.updateNickname(dto.getNickname().get()); }
        if (dto.getPhoneNumber().isPresent()) { member.updatePhoneNumber(dto.getPhoneNumber().get()); }
        if (dto.getProfileImageId().isPresent()) {
            Upload profileImage = uploadRepository.findByIdAndDeletedAtNull(dto.getProfileImageId().get())
                    .orElseThrow(NotFoundException::new);
            member.updateProfileImage(profileImage);
        }
        return MemberUpdateResponseDto.fromEntity(member);
    }


    /**
     * 탈퇴
     */
    public Long resign(Long memberId) {
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다."));
        member.delete();

        // 권한 삭제
        memberAuthorityRepository.findByMemberId(member.getId()).forEach(MemberAuthority::delete);
        
        return member.getId();
    }


    /**
     * 중복체크 유틸
     */
    private void validateEmailDuplication(String email) throws DuplicateKeyException {
        memberRepository.findByEmailAndDeletedAtNull(email)
                .ifPresent(m -> {
                    throw new DuplicateKeyException("이미 같은 이메일이 존재합니다.");
                });
    }

}
