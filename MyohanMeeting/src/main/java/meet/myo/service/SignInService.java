package meet.myo.service;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import meet.myo.domain.*;
import meet.myo.domain.authority.MemberAuthority;
import meet.myo.dto.request.member.MemberDirectCreateRequestDto;
import meet.myo.dto.request.member.MemberOauthCreateRequestDto;
import meet.myo.exception.NotFoundException;
import meet.myo.repository.*;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Transactional
@Service
@RequiredArgsConstructor
public class SignInService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailCertificationRepository emailCertificationRepository;
    private final AuthorityRepository authorityRepository;
    private final MemberAuthorityRepository memberAuthorityRepository;
    private final UploadRepository uploadRepository;
    private final EmailService emailService;


    /**
     * 회원가입(직접)
     */
    public Long directJoin(MemberDirectCreateRequestDto dto) {
        validateEmailDuplication(dto.getEmail());
        validateNicknameDuplication(dto.getNickname());

        String encoded = passwordEncoder.encode(dto.getPassword());
        Upload profileImage = uploadRepository.findByIdAndDeletedAtNull(1L).orElseThrow(NotFoundException::new);
        Member member = Member.directJoinBuilder()
                .email(dto.getEmail())
                .password(encoded)
                .nickname(dto.getNickname())
                .phoneNumber(dto.getPhoneNumber())
                .profileImage(profileImage)
                .build();
        MemberAuthority memberAuthority = MemberAuthority.createMemberAuthority(
                member, authorityRepository.findByAuthorityName("ROLE_USER").orElseThrow(NotFoundException::new));

        memberAuthorityRepository.save(memberAuthority);
        memberRepository.save(member);
        sendCertificationEmail(member);

        return member.getId();
    }


    /**
     * 회원가입(SNS)
     */
    public Long oauthJoin(MemberOauthCreateRequestDto dto) {

        validateEmailDuplication(dto.getEmail());
        validationOauthDuplication(dto.getOauthType(), dto.getOauthId());

        Upload profileImage = uploadRepository.findByIdAndDeletedAtNull(1L).orElseThrow(NotFoundException::new);
        Member.OauthJoinMemberBuilder builder = Member.oauthJoinBuilder()
                .oauthType(OauthType.valueOf(dto.getOauthType()))
                .oauthId(dto.getOauthId())
                .email(dto.getEmail())
                .profileImage(profileImage);
        if (dto.getNickname() != null) {
            validateNicknameDuplication(dto.getNickname());
            builder.nickname(dto.getNickname());
        } else {
            String randomNickname = createRandomNickname();
            validateNicknameDuplication(randomNickname);
            builder.nickname(randomNickname);
        }
        Member member = builder.build();

        MemberAuthority memberAuthority = MemberAuthority.createMemberAuthority(
                member, authorityRepository.findByAuthorityName("ROLE_USER").orElseThrow(NotFoundException::new));
        memberAuthorityRepository.save(memberAuthority);
        memberRepository.save(member);
        sendCertificationEmail(member);

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
     * 인증메일 재발송
     */
    public void resendCertMail(String email) {
        Member member = memberRepository.findByEmailAndDeletedAtNull(email).orElseThrow(NotFoundException::new);
        if (member.getCertified() == Certified.CERTIFIED) {
            throw new IllegalArgumentException("ALREADY_CERTIFIED");
        }
        sendCertificationEmail(member);
    }


    /**
     * 이메일 인증 CertCode 비교
     */
    public void verifyCertificationEmail(String certCode, String email) {

        EmailCertification latestCertification =
                emailCertificationRepository.findByCertCodeAndEmail(certCode, email)
                        .orElseThrow(() -> new NotFoundException("인증 메일 정보를 찾을 수 없습니다."));

        Member member = latestCertification.getMember();

        if (member.getCertified() == Certified.CERTIFIED) {
            throw new IllegalArgumentException("이미 인증된 회원입니다.");
        }

        if (latestCertification.isExpired()) {
            throw new IllegalArgumentException("인증코드가 만료되었습니다.");
        }

        member.updateCertified();
        MemberAuthority memberAuthority = MemberAuthority.createMemberAuthority(
                member, authorityRepository.findByAuthorityName("ROLE_USER").orElseThrow(NotFoundException::new));

        memberAuthorityRepository.save(memberAuthority);
        latestCertification.delete();
    }


    /**
     * 인증메일발송 및 CertCode 저장
     */
    private void sendCertificationEmail(Member member) {
        // 메일 발송
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
     * 중복체크 유틸
     */
    private void validateEmailDuplication(String email) throws DuplicateKeyException {
        memberRepository.findByEmailAndDeletedAtNull(email)
                .ifPresent(m -> {
                    throw new DuplicateKeyException("이미 같은 이메일이 존재합니다.");
                });
    }

    private void validationOauthDuplication(String oauthType, String oauthId) {
        memberRepository.findByOauth(OauthType.valueOf(oauthType), oauthId)
                .ifPresent(m -> {
                    throw new DuplicateKeyException("이미 해당 SNS 로그인 정보가 존재합니다.");
                });
    }

    private void validateNicknameDuplication(String nickname) throws DuplicateKeyException {
        memberRepository.findByNicknameAndDeletedAtNull(nickname)
                .ifPresent(m -> {
                    throw new DuplicateKeyException("이미 같은 닉네임이 존재합니다.");
                });
    }

    private String createRandomNickname() {
        return "user" + System.currentTimeMillis();
    }

}
