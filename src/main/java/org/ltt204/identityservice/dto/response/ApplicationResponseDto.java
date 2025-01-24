package org.ltt204.identityservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    @Builder.Default
    String message = "success";

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