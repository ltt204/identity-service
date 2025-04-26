package org.ltt204.identityservice.presentations.web.dtos.responses.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.ltt204.identityservice.presentations.web.advices.AppException;
import org.ltt204.identityservice.presentations.web.advices.ErrorCode;

@Data
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationResponseDto<T> {
    @Builder.Default
    long statusCode = 1000;
    String errorCode;
    String message;
    @Builder.Default
    boolean success = true;
    @Nullable
    T content;

    public static <T> ApplicationResponseDto<T> success(T content, String message) {
        return ApplicationResponseDto.<T>builder()
                .message(message)
                .content(content)
                .build();
    }

    public static <T> ApplicationResponseDto<T> success(T content) {
        return ApplicationResponseDto.<T>builder()
                .content(content)
                .build();
    }

    public static <T> ApplicationResponseDto<T> success(String message) {
        return ApplicationResponseDto.<T>builder()
                .message(message)
                .content(null)
                .build();
    }


    public static <T> ApplicationResponseDto<T> success() {
        return ApplicationResponseDto.<T>builder()
                .content(null)
                .build();
    }

    public static <T> ApplicationResponseDto<T> failure(ErrorCode exception) {
        return ApplicationResponseDto.<T>builder()
                .statusCode(exception.getCode())
                .errorCode(exception.getErrorCode())
                .success(false)
                .message(exception.getErrorCode())
                .build();
    }

    public static<T> ApplicationResponseDto<T> failure(AppException exception) {
        return ApplicationResponseDto.<T>builder()
                .statusCode(exception.getError().getCode())
                .errorCode(exception.getError().getErrorCode())
                .success(false)
                .message(exception.getMessage())
                .build();
    }
}