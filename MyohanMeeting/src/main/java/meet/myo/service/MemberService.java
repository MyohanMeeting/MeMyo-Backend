package meet.myo.service;

import jakarta.mail.MessagingException;
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

import java.io.UnsupportedEncodingException;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailCertificationRepository emailCertificationRepository;
    private final AuthorityRepository authorityRepository;
    private final MemberAuthorityRepository memberAuthorityRepository;
    private final UploadRepository uploadRepository;
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
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
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
