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
        @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(
                schema = @Schema(implementation = ErrorResponseDto.class), examples = {@ExampleObject(value = """
{
  "status": "400 BAD_REQUEST",
  "timestamp": "2023-06-10T09:19:08.550Z",
  "message": "INVALID PARAMETER",
  "debugMessage": ""
}
""")})),
        @ApiResponse(responseCode = "405", description = "잘못된 메서드 또는 URI 요청입니다.", content = @Content(
                schema = @Schema(implementation = ErrorResponseDto.class), examples = { @ExampleObject(value = """
{
  "status": "405 METHOD_NOT_ALLOWED",
  "timestamp": "2023-06-10T09:19:08.550Z",
  "message": "METHOD_NOT_ALLOWED",
  "debugMessage": ""
}
""")})),
        @ApiResponse(responseCode = "500", description = "서버측 오류입니다.", content = @Content(
                schema = @Schema(implementation = ErrorResponseDto.class), examples = { @ExampleObject(value = """
{
  "status": "500 INTERNAL_SERVER_ERROR",
  "timestamp": "2023-06-10T09:19:08.550Z",
  "message": "SERVER ERROR",
  "debugMessage": ""
}
""")}))
})
public @interface ApiResponseCommon {
}
