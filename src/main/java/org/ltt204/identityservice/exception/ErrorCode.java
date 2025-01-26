package org.ltt204.identityservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED(9999, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    // 1xxx
    INVALID_ERROR_KEY(1001, "Invalid error key", HttpStatus.INTERNAL_SERVER_ERROR),

    // 2xxx
    FIRSTNAME_REQUIRED(2000, "Firstname is required", HttpStatus.BAD_REQUEST),
    LASTNAME_REQUIRED(2001, "Lastname is required", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME(2002, "Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(2003, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),

    // 3xxx
    CONFLICT(3000, "Resource already existed", HttpStatus.NOT_FOUND),
    NOT_FOUND(3001, "Resource not found", HttpStatus.NOT_FOUND),

    // 4xxx
    UNAUTHENTICATED(4000, "Authentication failed", HttpStatus.UNAUTHORIZED)
    ;

    private long code;
    @Setter
    private String message;

    private HttpStatusCode httpStatusCode;
}
