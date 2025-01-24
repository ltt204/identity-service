package org.ltt204.identityservice.exception.customererror;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ApplicationError {
    UNCATEGORIZED(9999, "Uncategorized exception"),
    CONFLICT(1001, "Resource conflicted"),
    NOT_FOUND(1002, "Resource not found"),
    FIRSTNAME_REQUIRED(1003, "Firstname is required"),
    LASTNAME_REQUIRED(1004, "Lastname is required"),
    INVALID_USERNAME(1005, "Username must be at least 3 characters"),
    INVALID_PASSWORD(1006, "Password must be at least 8 characters"),
    INVALID_ERROR_KEY(1009, "Invalid error key");

    private long code;
    @Setter
    private String message;
}
