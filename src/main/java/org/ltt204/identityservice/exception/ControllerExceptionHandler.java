package org.ltt204.identityservice.exception;

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

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<String> handlingMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest().body(Objects.requireNonNull(exception.getFieldError()).getDefaultMessage());
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    ResponseEntity<String> handlingResourceNotFoundException(ResourceNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(value = ResourceConflictException.class)
    ResponseEntity<String> handlingResourceConflictException(ResourceConflictException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }
}
