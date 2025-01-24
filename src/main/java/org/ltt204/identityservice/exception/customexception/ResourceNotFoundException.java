package org.ltt204.identityservice.exception.customexception;

import lombok.Getter;
import org.ltt204.identityservice.exception.customererror.ApplicationError;

@Getter
public class ResourceNotFoundException extends RuntimeException{
    private final ApplicationError error;

    public ResourceNotFoundException(ApplicationError error) {
        super(error.getMessage());
        this.error = error;
    }
}
