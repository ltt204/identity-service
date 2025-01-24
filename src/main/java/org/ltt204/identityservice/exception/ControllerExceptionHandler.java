package org.ltt204.identityservice.exception;

import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.ltt204.identityservice.dto.response.ApplicationResponseDto;
import org.ltt204.identityservice.exception.customererror.ApplicationError;
import org.ltt204.identityservice.exception.customexception.ResourceConflictException;
import org.ltt204.identityservice.exception.customexception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApplicationResponseDto<?>> handlingRuntimeException(RuntimeException exception) {
        var response = ApplicationResponseDto.failure(ApplicationError.UNCATEGORIZED);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApplicationResponseDto<?>> handlingMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        var enumKey = exception.getFieldError().getDefaultMessage();
        var error = ApplicationError.INVALID_ERROR_KEY;
        try {
            error = ApplicationError.valueOf(enumKey);
        } catch (IllegalIdentifierException e) {
            // TODO: Handle later
        }
        var response = ApplicationResponseDto.failure(error);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    ResponseEntity<ApplicationResponseDto<?>> handlingResourceNotFoundException(ResourceNotFoundException exception) {
        var response = ApplicationResponseDto.failure(exception.getError());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(value = ResourceConflictException.class)
    ResponseEntity<ApplicationResponseDto<?>> handlingResourceConflictException(ResourceConflictException exception) {
        var response = ApplicationResponseDto.failure(exception.getError());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

    }
}
