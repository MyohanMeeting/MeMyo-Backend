package meet.myo.springdoc.annotations;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import meet.myo.dto.response.ErrorResponseDto;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@ApiResponse(responseCode = "401", description = "인증이 필요한 URI입니다.", content = @Content(
        schema = @Schema(implementation = ErrorResponseDto.class), examples = { @ExampleObject(value = """
{
  "status": "401 UNAUTHORIZED",
  "timestamp": "2023-06-10T09:19:08.550Z",
  "message": "AUTHORIZATION REQUIRED",
  "debugMessage": ""
}
""")}))
public @interface ApiResponseSignin {
}
