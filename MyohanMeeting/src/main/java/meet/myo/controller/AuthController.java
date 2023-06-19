package meet.myo.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "인증 관련 기능")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {


}
