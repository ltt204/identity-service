package org.ltt204.identityservice.exception.customexception;

import org.ltt204.identityservice.exception.customererror.ApplicationError;

public class ResourceNotFoundException extends RuntimeException{
    private final ApplicationError error;

    public ResourceNotFoundException(ApplicationError error) {
        super(error.getMessage());
        this.error = error;
    }

    public ApplicationError getError() {
        return error;
    }
}
