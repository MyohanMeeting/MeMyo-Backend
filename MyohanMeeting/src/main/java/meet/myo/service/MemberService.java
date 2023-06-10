package meet.myo.service;

import lombok.RequiredArgsConstructor;
import meet.myo.domain.EmailCertification;
import meet.myo.domain.Member;
import meet.myo.domain.Oauth;
import meet.myo.domain.OauthType;
import meet.myo.domain.exception.NotFoundException;
import meet.myo.dto.request.*;
import meet.myo.dto.response.*;
import meet.myo.repository.EmailCertificationRepository;
import meet.myo.repository.MemberRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailCertificationRepository emailCertificationRepository;

    /**
     * 회원정보 조회
     */
    @Transactional(readOnly = true)
    public MemberResponseDto getMemberById(Long id) {
        return MemberResponseDto.fromEntity(memberRepository.findByIdAndDeletedAtNull(id).orElseThrow(() -> new NotFoundException("Member not found")));
    }

    /**
     * 회원가입(직접)
     */
    public Long directJoin(MemberDirectCreateRequestDto dto) {
        validateEmailDuplication(dto.getEmail());
        validateNickNameDuplication(dto.getNickName());
        String encoded = passwordEncoder.encode(dto.getPassword());
        Member member = Member.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .password(encoded)
                .nickName(dto.getNickName())
                .phoneNumber(dto.getPhoneNumber())
                .build();

        return memberRepository.save(member).getId();
    }

    /**
     * 회원가입(SNS)
     */
    public Long oauthJoin(MemberOauthCreateRequestDto dto) {
        validateEmailDuplication(dto.getEmail());

        Member member = Member.createMember(dto);
        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }


    /**
     * 이메일 중복체크
     */
    public void emailDuplicationCheck(EmailDuplicationCheckRequestDto dto) {
        validateEmailDuplication(dto.getEmail());
    }

    private void validateEmailDuplication(String email) throws DuplicateKeyException {
        memberRepository.findByEmailAndDeletedAtNull(email)
                .ifPresent(m -> {
                    throw new DuplicateKeyException("RESOURCE_DUPLICATION");
                });
    }


    /**
     * 닉네임 중복체크
     */
    public void nickNameDuplicationCheck(NickNameDuplicationCheckRequestDto dto) {
        validateNickNameDuplication(dto.getNickName());
    }

    private void validateNickNameDuplication(String nickName) throws DuplicateKeyException {
        memberRepository.findByNickNameAndDeletedAtNull(nickName)
                .ifPresent(m -> {
                    throw new DuplicateKeyException("RESOURCE_DUPLICATION");
                });
    }

    /**
     * 이메일 인증용 메일발송 및 UUID 저장
     */
    public void sendCertificationEmail(Long memberId) { // TODO: 리턴항목 생각
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("Not Found member"));

        EmailCertification emailCertification = EmailCertification.createEmailCertification(member);
        emailCertificationRepository.save(emailCertification);
        // TODO: 이메일 발송 로직 작성

    }

    /**
     * 이메일 인증 UUID 비교
     */
    public void verifyCertificationEmail(Long memberId, CertifyEmailRequestDto dto) {
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("Not Found member"));
        EmailCertification latestCertification = emailCertificationRepository.findLatestByMemberId(memberId)
                .orElseThrow(() -> new NotFoundException("Not Found email certification"));

        if (!latestCertification.getUUID().equals(dto.getUUID())) {
            throw new NotFoundException("Invalid UUID");
        }
        // TODO: If Expired 된 것들이 이곳에 작성되어야 할까요?

    }

    /**
     * 이메일 수정
     */
    @Transactional
    public EmailUpdateResponseDto updateEmail(Long memberId, EmailUpdateRequestDto dto) {
        String newEmail = dto.getNewEmail();
        validateEmailDuplication(newEmail); // 이메일 중복체크 추가
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid"));
        member.updateEmail(newEmail);

        return EmailUpdateResponseDto.fromEntity(member);
    }


    /**
     * Oauth 수정
     */
    public OauthUpdateResponseDto updateOauth(Long memberId, OauthUpdateRequestDto dto) {
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("Member not found"));

        Oauth updatedOauth = Oauth.createOauth(OauthType.valueOf(dto.getOauthType()), dto.getOauthId());
        member.updateOauth(updatedOauth);
        Member savedMember = memberRepository.save(member);

        return OauthUpdateResponseDto.fromEntity(savedMember);
    }

    /**
     * Oauth 삭제
     */
    public void deleteOauth(Long memberId) {
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("Member not found"));

        member.updateOauth(null);
        memberRepository.save(member);
    }


    /**
     * 비밀번호 수정
     */
    public PasswordUpdateResponseDto updatePassword(Long memberId, PasswordUpdateRequestDto dto) {
        String currentPassword = dto.getCurrentPassword();
        String newPassword = dto.getNewPassword();
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid"));

        if (!passwordEncoder.matches(currentPassword, member.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        String encodedPassword = passwordEncoder.encode(newPassword);
        member.updatePassword(encodedPassword);
        return PasswordUpdateResponseDto.fromEntity(member);
    }


    /**
     * 개인정보 수정
     */
    public MemberUpdateResponseDto updateMember(MemberUpdateRequestDto dto) {
        Long memberId = dto.getMemberId();
        String newName = dto.getName();
        String newNickName = dto.getNickName();
        String newPhoneNumber = dto.getPhoneNumber();

        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid"));

        member.updateName(newName);
        member.updateNickName(newNickName);
        member.updatePhoneNumber(newPhoneNumber);
        return MemberUpdateResponseDto.fromEntity(member);
    }


    /**
     * 탈퇴
     */
    public Long resign(Long memberId) {
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("Member not found"));
        member.delete();
        return 1L;
    }

}
