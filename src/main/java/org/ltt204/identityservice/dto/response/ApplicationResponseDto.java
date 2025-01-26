package org.ltt204.identityservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.ltt204.identityservice.exception.customererror.ApplicationError;

@Data
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationResponseDto<T> {
    @Builder.Default
    long code = 1000;
    String message;

    T content;

    public static <T> ApplicationResponseDto<T> success(T content) {
        return ApplicationResponseDto.<T>builder()
                .content(content)
                .build();
    }

    public static <T> ApplicationResponseDto<T> failure(ApplicationError exception) {
        return ApplicationResponseDto.<T>builder()
                .code(exception.getCode())
                .message(exception.getMessage())
                .build();
    }
}