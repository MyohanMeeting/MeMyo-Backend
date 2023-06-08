package meet.myo.service;

import lombok.RequiredArgsConstructor;
import meet.myo.dto.request.*;
import meet.myo.dto.response.*;
import meet.myo.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원정보 조회
     */
    @Transactional(readOnly = true)
    public MemberResponseDto getMemberById(Long id) {
        return MemberResponseDto.fromEntity();
    }

    /**
     * 회원가입(직접)
     */
    public Long directJoin(MemberDirectCreateRequestDto dto) {
        return 1L;
    }

    /**
     * 회원가입(SNS)
     */
    public Long oauthJoin(MemberOauthCreateRequestDto dto) {
        return 1L;
    }

    /**
     * 이메일 중복체크
     */
    public void emailDuplicationCheck(EmailDuplicationCheckRequestDto dto) {

    }

    /**
     * 닉네임 중복체크
     */
    public void nickNameDuplicationCheck(NickNameDuplicationCheckRequestDto dto) {

    }

    /**
     * 이메일 인증용 메일발송 및 UUID 저장
     */
    public void sendCertificationEmail(Long memberId) { // TODO: 리턴항목 생각

    }

    /**
     * 이메일 인증 UUID 비교
     */
    public void verifyCertificationEmail(Long memberId, CertifyEmailRequestDto dto) {

    }

    /**
     * 이메일 수정
     */
    public EmailUpdateResponseDto updateEmail(Long memberId, EmailUpdateRequestDto dto) {
        return EmailUpdateResponseDto.fromEntity();
    }

    /**
     * Oauth 수정
     */
    public OauthUpdateResponseDto updateOauth(Long memberId, OauthUpdateRequestDto dto) {
        return OauthUpdateResponseDto.fromEntity();
    }


    /**
     * Oauth 삭제
     */
    public void deleteOauth(Long memberId) {

    }


    /**
     * 비밀번호 수정
     */
    public void updatePassword(Long memberId, PasswordUpdateRequestDto dto) {
        //TODO: 리턴값 생각
    }


    /**
     * 개인정보 수정
     */
    public MemberUpdateResponseDto updateMember(Long memberId, MemberUpdateRequestDto dto) {
        return MemberUpdateResponseDto.fromEntity();
    }


    /**
     * 탈퇴
     */
    public Long resign(Long memberId) {
        return 1L;
    }

}
