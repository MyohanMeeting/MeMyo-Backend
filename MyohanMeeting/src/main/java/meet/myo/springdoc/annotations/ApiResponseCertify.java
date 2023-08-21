package meet.myo.springdoc.annotations;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import meet.myo.dto.response.ErrorResponseDto;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@ApiResponses(value = {
        @ApiResponse(responseCode = "403", description = "이메일 미인증 회원입니다.", content = @Content(
                schema = @Schema(implementation = ErrorResponseDto.class), examples = { @ExampleObject(value = """
{
  "status": "403 BAD_REQUEST",
  "timestamp": "2023-06-10T09:19:08.550Z",
  "message": "UNCERTIFIED",
  "debugMessage": ""
}
""")})),
})
public @interface ApiResponseCertify {
}
