package meet.myo.controller;

import lombok.RequiredArgsConstructor;
import meet.myo.dto.request.*;
import meet.myo.dto.response.*;
import meet.myo.service.MemberService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/member")
public class MemberController {

    private final MemberService memberService;

    /**
     * 직접 회원가입
     */
    @PostMapping("/direct")
    public CommonResponseDto<Map<String, Long>> joinDirectV1(@Validated @RequestBody final MemberDirectCreateRequestDto dto) {
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("memberId", memberService.directJoin(dto)))
                .build();
    }

    /**
     * SNS 회원가입
     */
    @PostMapping("/oauth")
    public CommonResponseDto<Map<String, Long>> joinOauthV1(@Validated @RequestBody final MemberOauthCreateRequestDto dto) {
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("memberId", memberService.oauthJoin(dto)))
                .build();
    }

    /**
     * 이메일 중복확인
     */
    @GetMapping("/email")
    public CommonResponseDto emailDuplicationCheckV1(
            @Validated @RequestBody final EmailDuplicationCheckRequestDto dto
    ) {
        memberService.emailDuplicationCheck(dto);
        return CommonResponseDto.builder().build();
    }

    /**
     * 닉네임 중복확인
     */
    @GetMapping("/nickName")
    public CommonResponseDto emailDuplicationCheckV1(
            @Validated @RequestBody final NickNameDuplicationCheckRequestDto dto
    ) {
        memberService.nickNameDuplicationCheck(dto);
        return CommonResponseDto.builder().build();
    }


    /**
     * 인증 이메일 발송
     */
    @PostMapping("/certification")
    public CommonResponseDto sendCertificationEmailV1() {
        Long memberId = 1L; //TODO: security
        memberService.sendCertificationEmail(memberId);
        return CommonResponseDto.builder().build();
    }

    /**
     * 메일인증 코드 검증
     */
    @PutMapping("/certification")
    public CommonResponseDto verifyCertificationEmailV1(@Validated @RequestBody final CertifyEmailRequestDto dto) {
        Long memberId = 1L; //TODO: security
        memberService.verifyCertificationEmail(memberId, dto);
        return CommonResponseDto.builder().build(); // TODO: 리턴값 어떻게 할지 생각
    }

    /**
     * 내 정보 보기
     */
    @GetMapping("")
    public CommonResponseDto<MemberResponseDto> getMyInfoV1() {
        Long memberId = 1L; //TODO: security
        return CommonResponseDto.<MemberResponseDto>builder()
                .data(memberService.getMemberById(memberId))
                .build();
    }

    /**
     * 내 정보 수정
     */
    @PatchMapping("")
    public CommonResponseDto<MemberUpdateResponseDto> updateMyInfoV1(@Validated @RequestBody final MemberUpdateRequestDto dto) {
        Long memberId = 1L; //TODO: security
        return CommonResponseDto.<MemberUpdateResponseDto>builder()
                .data(memberService.updateMember(memberId, dto))
                .build();
    }

    /**
     * 이메일 수정
     */
    @PutMapping("/email")
    public CommonResponseDto<EmailUpdateResponseDto> updateEmailV1(@Validated @RequestBody final EmailUpdateRequestDto dto) {
        Long memberId = 1L; //TODO: security
        return CommonResponseDto.<EmailUpdateResponseDto>builder()
                .data(memberService.updateEmail(memberId, dto))
                .build();
    }

    /**
     * 비밀번호 수정
     */
    @PutMapping("/password")
    public CommonResponseDto updatePasswordV1(@Validated @RequestBody final PasswordUpdateRequestDto dto) {
        Long memberId = 1L; //TODO: security
        memberService.updatePassword(memberId, dto);
        return CommonResponseDto.builder().build();
    }

    /**
     * SNS 로그인 정보 수정
     */
    @PutMapping("/oauth")
    public CommonResponseDto<OauthUpdateResponseDto> updateOauthV1(@Validated @RequestBody final OauthUpdateRequestDto dto) {
        Long memberId = 1L; //TODO: security
        return CommonResponseDto.<OauthUpdateResponseDto>builder()
                .data(memberService.updateOauth(memberId, dto))
                .build();
    }

    /**
     * SNS 로그인 정보 삭제
     */
    @DeleteMapping("/oauth")
    public CommonResponseDto removeOauthV1() {
        Long memberId = 1L; //TODO: security
        memberService.deleteOauth(memberId);
        return CommonResponseDto.builder().build();
    }

    /**
     * 탈퇴
     */
    @DeleteMapping("")
    public CommonResponseDto resignV1() {
        Long memberId = 1L; //TODO: security
        memberService.resign(memberId);
        return CommonResponseDto.builder().build();
    }
}
