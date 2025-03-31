package org.ltt204.identityservice.presentations.web.advices;

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
    INVALID_TOKEN_FORMAT(1002, "Invalid token format", HttpStatus.BAD_REQUEST),
    // 2xxx
    FIRSTNAME_REQUIRED(2000, "Firstname is required", HttpStatus.BAD_REQUEST),
    LASTNAME_REQUIRED(2001, "Lastname is required", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME(2002, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(2003, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_DATE_OF_BIRTH(2004, "You have to be at least {min} years old", HttpStatus.BAD_REQUEST),

    // 3xxx
    CONFLICT(3000, "Resource already existed", HttpStatus.NOT_FOUND),
    NOT_FOUND(3001, "Resource not found", HttpStatus.NOT_FOUND),

    // 4xxx
    UNAUTHENTICATED(4000, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(4001, "You are not authorized for this action", HttpStatus.FORBIDDEN),
    ;

    private long code;
    @Setter
    private String message;
    private HttpStatusCode httpStatusCode;

    public ErrorCode withMessage(String message) {
        this.setMessage(message);
        return this;
    }
}
