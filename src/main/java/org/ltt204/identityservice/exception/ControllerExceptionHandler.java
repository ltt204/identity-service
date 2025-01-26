package org.ltt204.identityservice.exception;

import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.ltt204.identityservice.dto.response.ApplicationResponseDto;
import org.ltt204.identityservice.exception.customererror.ErrorCode;
import org.ltt204.identityservice.exception.customexception.ResourceConflictException;
import org.ltt204.identityservice.exception.customexception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApplicationResponseDto<?>> handlingRuntimeException(RuntimeException exception) {
        var error = ErrorCode.UNCATEGORIZED;
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

    @ExceptionHandler(value = ResourceNotFoundException.class)
    ResponseEntity<ApplicationResponseDto<?>> handlingResourceNotFoundException(ResourceNotFoundException exception) {
        var error = exception.getError();
        var response = ApplicationResponseDto.failure(error);
        return ResponseEntity.status(error.getHttpStatusCode()).body(response);
    }

    @ExceptionHandler(value = ResourceConflictException.class)
    ResponseEntity<ApplicationResponseDto<?>> handlingResourceConflictException(ResourceConflictException exception) {
        var error = exception.getError();
        var response = ApplicationResponseDto.failure(error);
        return ResponseEntity.status(error.getHttpStatusCode()).body(response);

    }
}
