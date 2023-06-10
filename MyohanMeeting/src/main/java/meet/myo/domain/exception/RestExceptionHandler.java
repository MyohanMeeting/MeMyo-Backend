package meet.myo.domain.exception;

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
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        String errorMessage = "Invalid request parameter: " + ex.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    // 401 특정 권한 없을때
    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder(ex, HttpStatus.UNAUTHORIZED, "인증 실패")
            .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

    // 403 데이터 접근 오류
    @ExceptionHandler(DataAccessException.class)
    protected ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder(ex, HttpStatus.FORBIDDEN, "Access denied")
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    // 404 리소스 not found
    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder(ex, HttpStatus.NOT_FOUND, "Resource not found")
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    // 409 Duplicate Resource create Conflict
    @ExceptionHandler(DuplicateKeyException.class)
    protected ResponseEntity<ErrorResponse> handleDuplicateKeyException(DuplicateKeyException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder(ex, HttpStatus.CONFLICT, "Duplicate resource")
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}
