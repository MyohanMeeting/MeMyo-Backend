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
        @ApiResponse(responseCode = "401", description = "인증이 필요한 URI입니다.", content = @Content(
                schema = @Schema(implementation = ErrorResponseDto.class), examples = { @ExampleObject(value = """
{
  "status": "401 UNAUTHORIZED",
  "timestamp": "2023-06-10T09:19:08.550Z",
  "message": "AUTHORIZATION REQUIRED",
  "debugMessage": ""
}
""")})),
        @ApiResponse(responseCode = "403", description = "허가된 접근이 아닙니다.", content = @Content(
                schema = @Schema(implementation = ErrorResponseDto.class), examples = { @ExampleObject(value = """
{
  "status": "403 BAD_REQUEST",
  "timestamp": "2023-06-10T09:19:08.550Z",
  "message": "ACCESS DENIED",
  "debugMessage": ""
}
""")}))
})
public @interface ApiResponseAuthority {
}
