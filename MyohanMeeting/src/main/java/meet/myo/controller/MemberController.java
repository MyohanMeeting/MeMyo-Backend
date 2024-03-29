package meet.myo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import meet.myo.config.SecurityUtil;
import meet.myo.dto.request.member.*;
import meet.myo.dto.response.*;
import meet.myo.dto.response.member.EmailUpdateResponseDto;
import meet.myo.dto.response.member.MemberResponseDto;
import meet.myo.dto.response.member.MemberUpdateResponseDto;
import meet.myo.dto.response.member.OauthRegisterResponseDto;
import meet.myo.exception.NotAuthenticatedException;
import meet.myo.service.MemberService;
import meet.myo.service.SignInService;
import meet.myo.springdoc.annotations.ApiResponseCommon;
import meet.myo.springdoc.annotations.ApiResponseSignin;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/member")
public class MemberController {

    private final MemberService memberService;
    private final SignInService signInService;

    /**
     * 직접 회원가입
     */
    @Tag(name = "1. Sign Up", description = "회원가입 관련 기능")
    @Operation(summary = "직접 가입", description = "이메일과 비밀번호로 직접 가입합니다.", operationId = "directJoin")
    @ApiResponse(responseCode = "200", description = "작성 성공", content = @Content(
            schema = @Schema(implementation = CommonResponseDto.class), examples = { @ExampleObject(value = """
{
  "status": "200 OK",
  "timestamp": "2023-06-10T09:19:08.550Z",
  "message": "SUCCESS",
  "data": {
    "memberId" : 1
  }
}
""")})) @ApiResponseCommon
    @SecurityRequirement(name = "")
    @PostMapping("/direct")
    public CommonResponseDto<Map<String, Long>> joinDirectV1(@Valid @RequestBody final MemberDirectCreateRequestDto dto) {
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("memberId", signInService.directJoin(dto)))
                .build();
    }

    /**
     * SNS 회원가입
     */
    @Tag(name = "1. Sign Up", description = "회원가입 관련 기능")
    @Operation(summary = "SNS 가입", description = "SNS 서비스를 통해 가입합니다.", operationId = "oauthJoin")
    @ApiResponse(responseCode = "200", description = "작성 성공", content = @Content(
            schema = @Schema(implementation = CommonResponseDto.class), examples = { @ExampleObject(value = """
{
  "status": "200 OK",
  "timestamp": "2023-06-10T09:19:08.550Z",
  "message": "SUCCESS",
  "data": {
    "memberId" : 1
  }
}
""")})) @ApiResponseCommon
    @SecurityRequirement(name = "")
    @PostMapping("/oauth")
    public CommonResponseDto<Map<String, Long>> joinOauthV1(@Valid @RequestBody final MemberOauthCreateRequestDto dto) {
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("memberId", signInService.oauthJoin(dto)))
                .build();
    }

    /**
     * 이메일 중복확인
     */
    @Tag(name = "1. Sign Up", description = "회원가입 관련 기능")
    @Operation(summary = "이메일 중복 확인", description = "가입 시 이메일 중복여부를 확인합니다.", operationId = "emailDuplicationCheck")
    @ApiResponse(responseCode = "200") @ApiResponseCommon
    @SecurityRequirement(name = "")
    @GetMapping("/email")
    public CommonResponseDto emailDuplicationCheckV1(
            @Parameter(name = "email", description = "중복을 확인할 이메일입니다.", in = ParameterIn.QUERY, example = "dupltest@test.com")
            @RequestParam(value = "email") @Valid @Email(message = "{validation.Email}") String email
    ) {
        signInService.emailDuplicationCheck(email);
        return CommonResponseDto.builder().build();
    }

    /**
     * 닉네임 중복확인
     */
    @Tag(name = "1. Sign Up", description = "회원가입 관련 기능")
    @Operation(summary = "닉네임 중복확인", description = "가입 시 닉네임 중복여부를 확인합니다.", operationId = "nicknameDuplicationCheck")
    @ApiResponse(responseCode = "200") @ApiResponseCommon
    @SecurityRequirement(name = "")
    @GetMapping("/nickname")
    public CommonResponseDto nicknameDuplicationCheckV1(
            @Parameter(name = "nickname", description = "중복을 확인할 닉네임입니다.", in = ParameterIn.QUERY)
            @RequestParam(value = "nickname") @Valid @Size(min = 2, max = 12, message = "{validation.Size}") String nickname
    ) {
        signInService.nicknameDuplicationCheck(nickname);
        return CommonResponseDto.builder().build();
    }

    /**
     * 인증 이메일 발송
     */
    @Tag(name = "1. Sign Up", description = "회원가입 관련 기능")
    @Operation(summary = "인증 이메일 재발송", description = "이메일 인증 코드가 만료되었을 경우 재발송 요청을 보냅니다.", operationId = "sendCertificationEmail")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseSignin
    @SecurityRequirement(name = "")
    @PostMapping("/certification")
    public CommonResponseDto sendCertificationEmailV1(
            @Parameter(name = "email", description = "회원 가입한 이메일입니다.", in = ParameterIn.QUERY, example = "myemail@email.com")
            @RequestParam(value = "email") @Valid @Email(message = "{validation.Email}") String email) {
        signInService.resendCertMail(email);
        return CommonResponseDto.builder().build();
    }

    /**
     * 메일인증 코드 검증
     */
    @Tag(name = "1. Sign Up", description = "회원가입 관련 기능")
    @Operation(summary = "메일인증 코드 검증", description = "이메일 주소 인증코드를 검증합니다.", operationId = "verifyCertificationEmail")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseSignin
    @SecurityRequirement(name = "")
    @PutMapping("/certification")
    public CommonResponseDto verifyCertificationEmailV1(
            @Parameter(name = "certCode", description = "메일로 전달된 인증 코드입니다.", in = ParameterIn.QUERY, example = "123456")
            @RequestParam(value = "certCode") String certCode,
            @Parameter(name = "email", description = "회원 가입한 이메일입니다.", in = ParameterIn.QUERY, example = "myemail@email.com")
            @RequestParam(value = "email") @Valid @Email(message = "{validation.Email}") String email
    ) {
        signInService.verifyCertificationEmail(certCode, email);
        return CommonResponseDto.builder().build(); // TODO: 리턴값 어떻게 할지 생각
    }

    /**
     * 내 정보 보기
     */
    @Tag(name = "2. Member", description = "회원 관련 기능")
    @Operation(summary = "내 정보 보기", description = "자신의 회원정보를 확인합니다.", operationId = "getMyInfo")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseSignin
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping("")
    public CommonResponseDto<MemberResponseDto> getMyInfoV1() {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID_ID"));
        return CommonResponseDto.<MemberResponseDto>builder()
                .data(memberService.getMemberById(memberId))
                .build();
    }

    /**
     * 내 정보 수정
     */
    @Tag(name = "2. Member", description = "회원 관련 기능")
    @Operation(summary = "내 정보 수정", description = "자신의 회원정보를 수정합니다.", operationId = "updateMyInfo")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseSignin
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PatchMapping("")
    public CommonResponseDto<MemberUpdateResponseDto> updateMyInfoV1(@RequestBody final MemberUpdateRequestDto dto) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID_ID"));
        return CommonResponseDto.<MemberUpdateResponseDto>builder()
                .data(memberService.updateMember(memberId, dto))
                .build();
    }

    /**
     * 이메일 수정
     */
    @Tag(name = "2. Member", description = "회원 관련 기능")
    @Operation(summary = "이메일 수정", description = "이메일을 수정합니다.", operationId = "updateEmail")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseSignin
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PutMapping("/email")
    public CommonResponseDto<EmailUpdateResponseDto> updateEmailV1(@Valid @RequestBody final EmailUpdateRequestDto dto) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID_ID"));
        return CommonResponseDto.<EmailUpdateResponseDto>builder()
                .data(memberService.updateEmail(memberId, dto))
                .build();
    }

    /**
     * 비밀번호 수정
     */
    @Tag(name = "2. Member", description = "회원 관련 기능")
    @Operation(summary = "비밀번호 수정",
            description = "로그인에 사용되는 비밀번호를 수정하거나, 비밀번호를 설정한 적 없는 SNS 회원의 경우 비밀번호를 새롭게 설정합니다.",
            operationId = "updatePassword")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseSignin
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PutMapping("/password")
    public CommonResponseDto updatePasswordV1(@RequestBody final PasswordUpdateRequestDto dto) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID_ID"));
        memberService.updatePassword(memberId, dto);
        return CommonResponseDto.builder().build();
    }

    /**
     * SNS 연결
     */
    @Tag(name = "2. Member", description = "회원 관련 기능")
    @Operation(summary = "SNS 로그인 연결",
            description = "로그인에 사용할 SNS를 새롭게 연결합니다. 이미 SNS 로그인 정보가 등록되어 있을 경우 먼저 해제가 필요합니다.",
            operationId = "registerOauth")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseSignin
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PostMapping("/oauthInfo")
    public CommonResponseDto<OauthRegisterResponseDto> registerOauthV1(@Valid @RequestBody final OauthRegisterRequestDto dto) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID_ID"));
        return CommonResponseDto.<OauthRegisterResponseDto>builder()
                .data(memberService.registerOauth(memberId, dto))
                .build();
    }

    /**
     * SNS 로그인 정보 삭제
     */
    @Tag(name = "2. Member", description = "회원 관련 기능")
    @Operation(summary = "SNS 연결 해제", description = "연결된 SNS 로그인 정보를 해제합니다. 비밀번호를 등록한 회원만 해제가 가능합니다.", operationId = "removeOauth")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseSignin
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @DeleteMapping("/oauthInfo")
    public CommonResponseDto removeOauthV1(@Valid @RequestBody OauthDeleteRequestDto dto) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID_ID"));
        memberService.deleteOauth(memberId, dto);
        return CommonResponseDto.builder().build();
    }

    /**
     * 탈퇴
     */
    @Tag(name = "2. Member", description = "회원 관련 기능")
    @Operation(summary = "회원 탈퇴", description = "서비스에서 탈퇴합니다.", operationId = "resign")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseSignin
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @DeleteMapping("")
    public CommonResponseDto resignV1() {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID_ID"));
        memberService.resign(memberId);
        return CommonResponseDto.builder().build();
    }
}
