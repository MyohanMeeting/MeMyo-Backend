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
    public Long directOauth(MemberOauthCreateRequestDto dto) {
        return 1L;
    }

    /**
     * 이메일 인증용 메일발송 및 UUID 저장
     */
    public void sendCertificationEmail() { // TODO: 별 리턴내용이 없긴한데 그래도 void는 아닌거같죠...?

    }

    /**
     * 이메일 인증 UUID 비교
     */
    public void verifyCertificationEmail(CertifyEmailRequestDto dto) {

    }

    /**
     * 이메일 수정
     */
    public EmailUpdateResponseDto updateEmail(EmailUpdateRequestDto dto) {
        return EmailUpdateResponseDto.fromEntity();
    }

    /**
     * Oauth 수정
     */
    public OauthUpdateResponseDto updateOauth(OauthUpdateRequestDto dto) {
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
    public PasswordUpdateResponseDto updatePassword(PasswordUpdateRequestDto dto) {
        return PasswordUpdateResponseDto.fromEntity();
    }


    /**
     * 개인정보 수정
     */
    public MemberUpdateResponseDto updateMember(MemberUpdateRequestDto dto) {
        return MemberUpdateResponseDto.fromEntity();
    }


    /**
     * 탈퇴
     */
    public Long resign(Long id) {
        return 1L;
    }

}
