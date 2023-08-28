package meet.myo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import meet.myo.dto.request.auth.DirectSignInRequestDto;
import meet.myo.dto.request.auth.OauthSignInRequestDto;
import meet.myo.dto.request.auth.TokenRefreshRequestDto;
import meet.myo.dto.response.CommonResponseDto;
import meet.myo.dto.response.SignInResponseDto;
import meet.myo.jwt.TokenDto;
import meet.myo.service.AuthService;
import meet.myo.springdoc.annotations.ApiResponseAuthority;
import meet.myo.springdoc.annotations.ApiResponseCertify;
import meet.myo.springdoc.annotations.ApiResponseCommon;
import meet.myo.springdoc.annotations.ApiResponseSignin;
import org.springframework.web.bind.annotation.*;

@Tag(name = "0. Authentication", description = "인증 관련 기능")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {
    private final AuthService authService;

    /**
     * 이메일 로그인
     */
    @Operation(summary = "이메일 회원 로그인", description = "이메일과 비밀번호로 직접 로그인합니다.", operationId = "directSignIn")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseCertify
    @SecurityRequirement(name = "")
    @PostMapping("/signin/direct")
    public CommonResponseDto<SignInResponseDto> directSignInV1(@Valid @RequestBody final DirectSignInRequestDto dto) {
        return CommonResponseDto.<SignInResponseDto>builder()
                .data(authService.signIn(dto))
                .build();
    }

    /**
     * oauth 로그인
     */
    @Operation(summary = "SNS 회원 로그인", description = "SNS 정보로 로그인합니다.", operationId = "oauthSignIn")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseCertify
    @SecurityRequirement(name = "")
    @PostMapping("/signin/oauth")
    public CommonResponseDto<SignInResponseDto> oauthSignInV1(@Valid @RequestBody final OauthSignInRequestDto dto) {
        return CommonResponseDto.<SignInResponseDto>builder()
                .data(authService.signIn(dto))
                .build();
    }

    /**
     * 토큰 리프레시
     */
    @Operation(summary = "토큰 리프레시", description = "토큰 리프레시를 요청합니다.", operationId = "refreshToken")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseSignin @ApiResponseAuthority
    @SecurityRequirement(name = "Authorization")
    @PostMapping("/refresh")
    public CommonResponseDto<TokenDto> refreshTokenV1(@Valid @RequestBody final TokenRefreshRequestDto dto) {
        return CommonResponseDto.<TokenDto>builder()
                .data(authService.tokenRefresh(dto.getRefreshToken()))
                .build();
    }

    /**
     * 로그아웃
     */
    @Operation(summary = "로그아웃", description = "로그아웃(토큰을 만료시킴)합니다.", operationId = "signOut")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseSignin @ApiResponseAuthority
    @SecurityRequirement(name = "Authorization")
    @PostMapping("/refresh")
    public CommonResponseDto signOutV1(@RequestHeader("Authorization") String token) {
        authService.signOut(token);
        return CommonResponseDto.builder().build();
    }
}
