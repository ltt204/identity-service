package org.ltt204.identityservice.exception.customexception;

public class ResourceConflictException extends RuntimeException{
    public ResourceConflictException(String message) {
        super(message);
    }
}
