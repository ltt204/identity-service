package org.ltt204.identityservice.exception.customexception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.ltt204.identityservice.exception.customererror.ErrorCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ResourceNotFoundException extends RuntimeException {
    ErrorCode error;

    public ResourceNotFoundException(ErrorCode error) {
        super(error.getMessage());
        this.error = error;
    }
}
