package meet.myo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import meet.myo.dto.request.auth.DirectSignInRequestDto;
import meet.myo.dto.request.auth.OauthSignInRequestDto;
import meet.myo.dto.request.auth.TokenRefreshRequestDto;
import meet.myo.dto.response.CommonResponseDto;
import meet.myo.jwt.TokenDto;
import meet.myo.service.AuthService;
import meet.myo.springdoc.annotations.ApiResponseAuthority;
import meet.myo.springdoc.annotations.ApiResponseCommon;
import meet.myo.springdoc.annotations.ApiResponseSignin;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "인증 관련 기능")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {
    private final AuthService authService;

    /**
     * 이메일 로그인
     */
    @Operation(summary = "이메일 회원 로그인", description = "이메일과 비밀번호로 직접 로그인합니다.", operationId = "directSignIn")
    @ApiResponse(responseCode = "200") @ApiResponseCommon
    @SecurityRequirement(name = "")
    @PostMapping("/signin/direct")
    public CommonResponseDto<TokenDto> directSignInV1(
            @Validated @RequestBody final DirectSignInRequestDto dto
    ) {
        return CommonResponseDto.<TokenDto>builder()
                .data(authService.getToken(dto))
                .build();
    }

    /**
     * oauth 로그인
     */
    @Operation(summary = "SNS 회원 로그인", description = "SNS 정보로 로그인합니다.", operationId = "oauthSignIn")
    @ApiResponse(responseCode = "200") @ApiResponseCommon
    @SecurityRequirement(name = "")
    @PostMapping("/signin/oauth")
    public CommonResponseDto<TokenDto> oauthSignInV1(
            @Validated @RequestBody final OauthSignInRequestDto dto
    ) {
        return CommonResponseDto.<TokenDto>builder()
                .data(authService.getToken(dto))
                .build();
    }

    /**
     * 토큰 리프레시
     */
    @Operation(summary = "토큰 리프레시", description = "토큰 리프레시를 요청합니다.", operationId = "refreshToken")
    @ApiResponse(responseCode = "200") @ApiResponseCommon @ApiResponseSignin @ApiResponseAuthority
    @SecurityRequirement(name = "")
    @PostMapping("/refresh")
    public CommonResponseDto<TokenDto> refreshTokenV1(
            @Validated @RequestBody final TokenRefreshRequestDto dto
    ) {
        return CommonResponseDto.<TokenDto>builder()
                .data(authService.tokenRefresh(dto.getRefreshToken()))
                .build();
    }
}
