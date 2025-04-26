package org.ltt204.identityservice.presentations.web.advices;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;


@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppException extends RuntimeException {
    ErrorCode error;
    String message;
    public AppException(ErrorCode error, String message) {
        super(message);
        this.message = message;
        this.error = error;
    }

    public AppException(ErrorCode error) {
        super(error.getErrorCode());
        this.error = error;
        this.message = error.getErrorCode();
    }
}
