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
@ApiResponse(responseCode = "404", description = "요청한 자원을 찾을 수 없습니다.", content = @Content(
        schema = @Schema(implementation = ErrorResponseDto.class), examples = { @ExampleObject(value = """
{
  "status": "404 NOT_FOUND",
  "timestamp": "2023-06-10T09:19:08.550Z",
  "message": "NO RESOURCE FOUND",
  "debugMessage": ""
}
""")}))
public @interface ApiResponseResource {
}
