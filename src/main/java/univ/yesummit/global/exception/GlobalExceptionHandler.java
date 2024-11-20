package univ.yesummit.global.exception;

import com.auth0.jwt.exceptions.TokenExpiredException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import univ.yesummit.domain.member.exception.MemberException;
import univ.yesummit.global.exception.dto.ErrorResponseVO;


import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 전역 예외 처리를 담당하는 GlobalExceptionHandler 클래스
     * @RestControllerAdvice를 통해 전역적으로 예외를 처리할 수 있는 어노테이션을 사용
     * 각 예외 처리 메서드는 특정한 예외가 발생했을 때 처리하고,
     * ErrorResponseVO라는 객체를 반환하여 일관된 에러 응답을 제공
     */

    @ExceptionHandler(MemberException.class)
    public ErrorResponseVO handleMemberException(MemberException e) {
        ErrorCode errorCode = e.getErrorCode();

        return getErrorResponse(errorCode);
    }

    @ExceptionHandler(value = JwtException.class)
    public ResponseEntity<String> handleJwtException(JwtException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token.");
    }


    @ResponseStatus(org.springframework.http.HttpStatus.FORBIDDEN)
    @ExceptionHandler(TokenExpiredException.class)
    public ErrorResponseVO handleTokenExpiredException(TokenExpiredException ex) {
        return ErrorResponseVO.builder()
                .name(ErrorCode.EXPIRED_ACCESS_TOKEN.name())
                .errorCode(ErrorCode.EXPIRED_ACCESS_TOKEN.getErrorCode())
                .message(ErrorCode.EXPIRED_ACCESS_TOKEN.getMessage()).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponseVO handleValidationException(MethodArgumentNotValidException ex) {
        return ErrorResponseVO.builder()
                .name("VALIDATION_ERROR")
                .errorCode(ex.getStatusCode().value())
                .message(Objects.requireNonNull(ex.getFieldError()).getDefaultMessage()).build();
    }

    private ErrorResponseVO getErrorResponse(ErrorCode errorCode) {
        return ErrorResponseVO.builder()
                .name(errorCode.name())
                .errorCode(errorCode.getErrorCode())
                .message(errorCode.getMessage()).build();
    }
}
