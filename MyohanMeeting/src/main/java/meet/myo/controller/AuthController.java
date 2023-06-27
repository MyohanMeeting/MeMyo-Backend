package meet.myo.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import meet.myo.dto.request.auth.DirectSignInRequestDto;
import meet.myo.dto.request.auth.OauthSignInRequestDto;
import meet.myo.dto.response.CommonResponseDto;
import meet.myo.jwt.TokenDto;
import meet.myo.service.AuthService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "인증 관련 기능")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {
    private final AuthService authService;

    @GetMapping("/signin/direct")
    public CommonResponseDto<TokenDto> directSignInV1(@Validated @RequestBody final DirectSignInRequestDto dto) {
        return CommonResponseDto.<TokenDto>builder()
                .data(authService.getToken(dto))
                .build();
    }

    @GetMapping("/signin/oauth")
    public CommonResponseDto<TokenDto> oauthSignInV1(@Validated @RequestBody final OauthSignInRequestDto dto) {
        return CommonResponseDto.<TokenDto>builder()
                .data(authService.getToken(dto))
                .build();
    }
}
