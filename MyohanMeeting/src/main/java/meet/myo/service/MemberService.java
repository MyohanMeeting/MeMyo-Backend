package meet.myo.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import meet.myo.domain.EmailCertification;
import meet.myo.domain.Member;
import meet.myo.domain.Oauth;
import meet.myo.domain.OauthType;
import meet.myo.domain.authority.MemberAuthority;
import meet.myo.dto.request.member.*;
import meet.myo.dto.response.member.EmailUpdateResponseDto;
import meet.myo.dto.response.member.MemberResponseDto;
import meet.myo.dto.response.member.MemberUpdateResponseDto;
import meet.myo.dto.response.member.OauthUpdateResponseDto;
import meet.myo.exception.NotFoundException;
import meet.myo.repository.AuthorityRepository;
import meet.myo.repository.EmailCertificationRepository;
import meet.myo.repository.MemberAuthorityRepository;
import meet.myo.repository.MemberRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailCertificationRepository emailCertificationRepository;
    private final AuthorityRepository authorityRepository;
    private final MemberAuthorityRepository memberAuthorityRepository;
    private final EmailService emailService;

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
     * 회원가입(직접)
     */
    public Long directJoin(MemberDirectCreateRequestDto dto) {
        validateEmailDuplication(dto.getEmail());
        validateNicknameDuplication(dto.getNickname());

        String encoded = passwordEncoder.encode(dto.getPassword());
        Member member = Member.directJoinBuilder()
                .email(dto.getEmail())
                .password(encoded)
                .nickname(dto.getNickname())
                .phoneNumber(dto.getPhoneNumber())
                .build();
        MemberAuthority memberAuthority = MemberAuthority.createMemberAuthority(
                member, authorityRepository.findByAuthorityName("ROLE_USER").orElseThrow(NotFoundException::new));

        memberAuthorityRepository.save(memberAuthority);
        return memberRepository.save(member).getId();
    }

    /**
     * 회원가입(SNS)
     */
    public Long oauthJoin(MemberOauthCreateRequestDto dto) {

        validateEmailDuplication(dto.getEmail());

        Member.OauthJoinMemberBuilder memberBuilder = Member.oauthJoinBuilder()
                .oauthType(dto.getOauthType() != null ? OauthType.valueOf(dto.getOauthType()) : null)
                .oauthId(dto.getOauthId())
                .email(dto.getEmail());
        if (dto.getNickname() != null) {
            validateNicknameDuplication(dto.getNickname());
            memberBuilder.nickname(dto.getNickname());
        } else {
            String randomNickname = createRandomNickname();
            validateNicknameDuplication(randomNickname);
            memberBuilder.nickname(randomNickname);
        }
        Member member = memberBuilder.build();
        MemberAuthority memberAuthority = MemberAuthority.createMemberAuthority(
                member, authorityRepository.findByAuthorityName("ROLE_USER").orElseThrow(NotFoundException::new));

        memberAuthorityRepository.save(memberAuthority);
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 이메일 중복체크
     */
    public void emailDuplicationCheck(String email) {
        validateEmailDuplication(email);
    }

    /**
     * 닉네임 중복체크
     */
    public void nicknameDuplicationCheck(String nickname) {
        validateNicknameDuplication(nickname);
    }

    /**
     * 이메일 인증용 메일발송 및 CertCode 저장
     */
    public void sendCertificationEmail(Long memberId) { // TODO: 리턴항목 생각
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다."));
        // 메일은 이곳에서 발송
        try {
            EmailCertification emailCertification = EmailCertification.createEmailCertification(member);
            emailCertificationRepository.save(emailCertification);
            emailService.sendEmail(member.getEmail(), emailCertification.getCertCode());

        } catch (MessagingException | UnsupportedEncodingException e) {
            // TODO: 에러 처리 로직 추가
            throw new RuntimeException("메일 전송에 실패했습니다", e);
        }
    }

    /**
     * 이메일 인증 CertCode 비교
     */
    public void verifyCertificationEmail(Long memberId, CertifyEmailRequestDto dto) {
        EmailCertification latestCertification =
                emailCertificationRepository.findByMemberIdAndCertCode(memberId, dto.getCertCode())
                .orElseThrow(() -> new NotFoundException("해당하는 이메일을 찾을 수 없습니다."));

        if (latestCertification.isExpired()) {
            throw new RuntimeException("인증코드가 만료되었습니다.");
        }
        Member member = latestCertification.getMember();
        member.updateCertified();
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
     * Oauth 수정
     */
    public OauthUpdateResponseDto updateOauth(Long memberId, OauthUpdateRequestDto dto) {
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다."));

        Oauth updatedOauth = Oauth.createOauth(OauthType.valueOf(dto.getOauthType()), dto.getOauthId()); //TODO: null check
        member.updateOauth(updatedOauth);

        return OauthUpdateResponseDto.fromEntity(member);
    }

    /**
     * Oauth 삭제
     */
    public void deleteOauth(Long memberId) {
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다."));
        if (member.getPassword() == null) {
            throw new IllegalArgumentException("SNS 로그인 정보를 삭제하려면 먼저 비밀번호를 설정해야 합니다.");
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
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다."));

        member.updateNickname(dto.getNickname());
        member.updatePhoneNumber(dto.getPhoneNumber());
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

    private void validateNicknameDuplication(String nickname) throws DuplicateKeyException {
        memberRepository.findByNicknameAndDeletedAtNull(nickname)
                .ifPresent(m -> {
                    throw new DuplicateKeyException("이미 같은 닉네임이 존재합니다.");
                });
    }

    private String createRandomNickname() {
        return "nickname" + UUID.randomUUID();
    }

}
