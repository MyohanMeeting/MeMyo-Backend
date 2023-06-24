package meet.myo.exception;

import jakarta.el.MethodNotFoundException;
import meet.myo.dto.response.ErrorResponseDto;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.AuthenticationException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    // 400 요청파라미터
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponseDto.builder()
                        .status(HttpStatus.BAD_REQUEST.toString()) // http 관점의 오류메시지
                        .message("INVALID PARAMETER") // java 관점의 오류메시지
                        .debugMessage(ex.getMessage()) // 실제 디버깅에 도움되는 상세한 메시지
                        .build());
    }

    // http code
    // exception


    // 401 특정 권한 없을때
    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<ErrorResponseDto> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ErrorResponseDto.builder()
                        .status(HttpStatus.BAD_REQUEST.toString())
                        .message("AUTHORIZATION REQUIRED")
                        .debugMessage(ex.getMessage())
                        .build());
        }

    // 403 데이터 접근 오류
    @ExceptionHandler(DataAccessException.class)
    protected ResponseEntity<ErrorResponseDto> handleDataAccessException(DataAccessException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorResponseDto.builder()
                .status(HttpStatus.FORBIDDEN.toString())
                .message("ACCESS DENIED")
                .debugMessage(ex.getMessage())
                .build());
    }

    // 404 리소스 not found
    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<ErrorResponseDto> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponseDto.builder()
                .status(HttpStatus.NOT_FOUND.toString())
                .message("NO RESOURCE FOUND")
                .debugMessage(ex.getMessage())
                .build());
    }

    @ExceptionHandler(MethodNotFoundException.class)
    protected ResponseEntity<ErrorResponseDto> handleMethodNotFoundException(MethodNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(ErrorResponseDto.builder()
                .status(HttpStatus.METHOD_NOT_ALLOWED.toString())
                .message("METHOD NOT ALLOWED")
                .debugMessage(ex.getMessage())
                .build());
    }


    // 409 conflict


    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ErrorResponseDto> handleServerException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponseDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .message("SERVER ERROR")
                .debugMessage(ex.getMessage())
                .build());
    }

    // 409 Duplicate Resource create Conflict
    @ExceptionHandler(DuplicateKeyException.class)
    protected ResponseEntity<ErrorResponse> handleDuplicateKeyException(DuplicateKeyException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder(ex, HttpStatus.CONFLICT, "Duplicate resource")
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}
