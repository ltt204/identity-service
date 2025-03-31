package org.ltt204.identityservice.infra.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.ltt204.identityservice.presentations.web.dtos.responses.common.ApplicationResponseDto;
import org.ltt204.identityservice.presentations.web.advices.ErrorCode;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        var error = ErrorCode.UNAUTHENTICATED;

        // Config response's header
        response.setStatus(error.getHttpStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Config response's body
        ApplicationResponseDto<?> responseBody = ApplicationResponseDto.failure(error);

        ObjectMapper objectMapper = new ObjectMapper();

        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
        response.flushBuffer();
    }
}
