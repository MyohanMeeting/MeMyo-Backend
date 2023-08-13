package meet.myo.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import jakarta.el.MethodNotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import meet.myo.dto.response.ErrorResponseDto;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponseDto.builder()
                        .status(HttpStatus.BAD_REQUEST.toString())
                        .message("INVALID PARAMETER")
                        .debugMessage(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<ErrorResponseDto> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ErrorResponseDto.builder()
                        .status(HttpStatus.BAD_REQUEST.toString())
                        .message("AUTHORIZATION REQUIRED")
                        .debugMessage(ex.getMessage())
                        .build());
        }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponseDto> handleDataAccessException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorResponseDto.builder()
                .status(HttpStatus.FORBIDDEN.toString())
                .message("ACCESS DENIED")
                .debugMessage(ex.getMessage())
                .build());
    }

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

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ErrorResponseDto> handleServerException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponseDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .message("SERVER ERROR")
                .debugMessage(ex.getMessage())
                .build());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    protected ResponseEntity<ErrorResponseDto> handleDuplicateKeyException(DuplicateKeyException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ErrorResponseDto.builder()
                        .status(HttpStatus.CONFLICT.toString())
                        .message("Duplicate resource")
                        .debugMessage(ex.getMessage())
                        .build());
    }
}
