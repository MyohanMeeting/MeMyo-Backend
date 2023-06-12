package meet.myo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import meet.myo.dto.request.*;
import meet.myo.dto.response.*;
import meet.myo.service.MemberService;
import meet.myo.springdoc.annotations.ApiResponseCommon;
import meet.myo.springdoc.annotations.ApiResponseSignin;
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
    @Tag(name = "Sign Up", description = "회원가입 관련 기능")
    @Operation(summary = "직접 가입", description = "이메일과 비밀번호로 직접 가입합니다.", operationId = "directJoin")
    @ApiResponse(responseCode = "200") @ApiResponseCommon
    @SecurityRequirement(name = "")
    @PostMapping("/direct")
    public CommonResponseDto<Map<String, Long>> joinDirectV1(@Validated @RequestBody final MemberDirectCreateRequestDto dto) {
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("memberId", memberService.directJoin(dto)))
                .build();
    }

    /**
     * SNS 회원가입
     */
    @Tag(name = "Sign Up", description = "회원가입 관련 기능")
    @Operation(summary = "SNS 가입", description = "SNS 서비스를 통해 가입합니다.", operationId = "oauthJoin")
    @ApiResponse(responseCode = "200") @ApiResponseCommon
    @SecurityRequirement(name = "")
    @PostMapping("/oauth")
    public CommonResponseDto<Map<String, Long>> joinOauthV1(@Validated @RequestBody final MemberOauthCreateRequestDto dto) {
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("memberId", memberService.oauthJoin(dto)))
                .build();
    }

    /**
     * 이메일 중복확인
     */
    @Tag(name = "Sign Up", description = "회원가입 관련 기능")
    @Operation(summary = "이메일 중복 확인", description = "가입 시 이메일 중복여부를 확인합니다.", operationId = "emailDuplicationCheck")
    @ApiResponse(responseCode = "200") @ApiResponseCommon
    @SecurityRequirement(name = "")
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
    @Tag(name = "Sign Up", description = "회원가입 관련 기능")
    @Operation(summary = "닉네임 중복확인", description = "가입 시 닉네임 중복여부를 확인합니다.", operationId = "nickNameDuplicationCheck")
    @ApiResponse(responseCode = "200") @ApiResponseCommon
    @SecurityRequirement(name = "")
    @GetMapping("/nickName")
    public CommonResponseDto nickNameDuplicationCheckV1(
            @Validated @RequestBody final NickNameDuplicationCheckRequestDto dto
    ) {
        memberService.nickNameDuplicationCheck(dto);
        return CommonResponseDto.builder().build();
    }


    /**
     * 인증 이메일 발송
     */
    @Tag(name = "Sign Up", description = "회원가입 관련 기능")
    @Operation(summary = "인증 이메일 발송", description = "가입 후 이메일 주소 인증을 위한 이메일을 발송합니다.", operationId = "sendCertificationEmail")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseSignin
    @SecurityRequirement(name = "JWT")
    @PostMapping("/certification")
    public CommonResponseDto sendCertificationEmailV1() {
        Long memberId = 1L; //TODO: security
        memberService.sendCertificationEmail(memberId);
        return CommonResponseDto.builder().build();
    }

    /**
     * 메일인증 코드 검증
     */
    @Tag(name = "Sign Up", description = "회원가입 관련 기능")
    @Operation(summary = "메일인증 코드 검증", description = "이메일 주소 인증코드를 검증합니다.", operationId = "verifyCertificationEmail")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseSignin
    @SecurityRequirement(name = "JWT")
    @PutMapping("/certification")
    public CommonResponseDto verifyCertificationEmailV1(@Validated @RequestBody final CertifyEmailRequestDto dto) {
        Long memberId = 1L; //TODO: security
        memberService.verifyCertificationEmail(memberId, dto);
        return CommonResponseDto.builder().build(); // TODO: 리턴값 어떻게 할지 생각
    }

    /**
     * 내 정보 보기
     */
    @Tag(name = "Member", description = "회원 관련 기능")
    @Operation(summary = "내 정보 보기", description = "자신의 회원정보를 확인합니다.", operationId = "getMyInfo")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseSignin
    @SecurityRequirement(name = "JWT")
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
    @Tag(name = "Member", description = "회원 관련 기능")
    @Operation(summary = "내 정보 수정", description = "자신의 회원정보를 수정합니다.", operationId = "updateMyInfo")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseSignin
    @SecurityRequirement(name = "JWT")
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
    @Tag(name = "Member", description = "회원 관련 기능")
    @Operation(summary = "이메일 수정", description = "이메일을 수정합니다.", operationId = "updateEmail")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseSignin
    @SecurityRequirement(name = "JWT")
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
    @Tag(name = "Member", description = "회원 관련 기능")
    @Operation(summary = "비밀번호 수정",
            description = "로그인에 사용되는 비밀번호를 수정하거나, 비밀번호를 설정한 적 없는 SNS 회원의 경우 비밀번호를 새롭게 설정합니다.",
            operationId = "updatePassword")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseSignin
    @SecurityRequirement(name = "JWT")
    @PutMapping("/password")
    public CommonResponseDto updatePasswordV1(@Validated @RequestBody final PasswordUpdateRequestDto dto) {
        Long memberId = 1L; //TODO: security
        memberService.updatePassword(memberId, dto);
        return CommonResponseDto.builder().build();
    }

    /**
     * SNS 로그인 정보 수정
     */
    @Tag(name = "Member", description = "회원 관련 기능")
    @Operation(summary = "SNS 로그인 정보 수정",
            description = "SNS 로그인 정보를 수정하거나, SNS 로그인을 설정한 적 없는 직접 가입 회원의 경우 SNS 로그인 정보를 새롭게 연결합니다.",
            operationId = "updateOauth")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseSignin
    @SecurityRequirement(name = "JWT")
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
    @Tag(name = "Member", description = "회원 관련 기능")
    @Operation(summary = "SNS 로그인 정보 삭제", description = "연결된 SNS 로그인 정보를 삭제합니다.", operationId = "removeOauth")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseSignin
    @SecurityRequirement(name = "JWT")
    @DeleteMapping("/oauth")
    public CommonResponseDto removeOauthV1() {
        Long memberId = 1L; //TODO: security
        memberService.deleteOauth(memberId);
        return CommonResponseDto.builder().build();
    }

    /**
     * 탈퇴
     */
    @Tag(name = "Member", description = "회원 관련 기능")
    @Operation(summary = "회원 탈퇴", description = "서비스에서 탈퇴합니다.", operationId = "resign")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseSignin
    @SecurityRequirement(name = "JWT")
    @DeleteMapping("")
    public CommonResponseDto resignV1() {
        Long memberId = 1L; //TODO: security
        memberService.resign(memberId);
        return CommonResponseDto.builder().build();
    }
}
