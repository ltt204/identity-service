package org.ltt204.identityservice.exception.customererror;

public enum ApplicationError {
    UNCATEGORIZED(9999, "Uncategorized exception"),
    CONFLICT(1001, "Resource conflicted"),
    NOT_FOUND(1002, "Resource not found"),
    INVALID_USERNAME(1003, "Username must be at least 3 characters"),
    INVALID_PASSWORD(1004, "Password must be at least 8 characters"),
    INVALID_ERROR_KEY(1009, "Invalid error key")
    ;
    private final long code;
    private String message;

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    ApplicationError(long code, String message) {
        this.code = code;
        this.message = message;
    }
}
