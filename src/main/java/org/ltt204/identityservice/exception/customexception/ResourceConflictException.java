package org.ltt204.identityservice.exception.customexception;

import org.ltt204.identityservice.exception.customererror.ApplicationError;

public class ResourceConflictException extends RuntimeException {
    public ApplicationError getError() {
        return error;
    }

    private final ApplicationError error;
    public ResourceConflictException(ApplicationError error) {
        super(error.getMessage());

        this.error =error;
    }
}
