package org.ltt204.identityservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.ltt204.identityservice.dto.response.common.ApplicationResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApplicationResponseDto<?>> handlingRuntimeException(RuntimeException exception) {
        var error = ErrorCode.UNCATEGORIZED;
        log.error(exception.getMessage());
        var response = ApplicationResponseDto.failure(error);
        return ResponseEntity.status(error.getHttpStatusCode()).body(response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApplicationResponseDto<?>> handlingMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        var enumKey = exception.getFieldError().getDefaultMessage();
        var error = ErrorCode.INVALID_ERROR_KEY;
        try {
            error = ErrorCode.valueOf(enumKey);
        } catch (IllegalIdentifierException e) {
            // TODO: Handle later
        }
        var response = ApplicationResponseDto.failure(error);
        return ResponseEntity.status(error.getHttpStatusCode()).body(response);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApplicationResponseDto<?>> handlingAppException(AppException exception) {
        var error = exception.getError();
        var response = ApplicationResponseDto.failure(error);
        return ResponseEntity.status(error.getHttpStatusCode()).body(response);
    }

    @ExceptionHandler(value = AuthorizationDeniedException.class)
    ResponseEntity<ApplicationResponseDto<?>> handlingAuthorizeException(AuthorizationDeniedException exception) {
        var error = ErrorCode.UNAUTHORIZED;
        var response = ApplicationResponseDto.failure(error);
        return ResponseEntity.status(error.getHttpStatusCode()).body(response);
    }
}
