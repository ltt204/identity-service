package org.ltt204.identityservice.presentations.web.controllers;

import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.ltt204.identityservice.domain.services.interfaces.IAuthService;
import org.ltt204.identityservice.presentations.web.dtos.requests.auth.LogoutRequestDto;
import org.ltt204.identityservice.presentations.web.dtos.requests.auth.RevokeTokenRequestDto;
import org.ltt204.identityservice.presentations.web.dtos.requests.auth.TokenIntrospectRequestDto;
import org.ltt204.identityservice.presentations.web.dtos.requests.user.UserSignInRequestDto;
import org.ltt204.identityservice.presentations.web.dtos.responses.auth.AuthenticationResponseDto;
import org.ltt204.identityservice.presentations.web.dtos.responses.auth.IntrospectResponseDto;
import org.ltt204.identityservice.presentations.web.dtos.responses.common.ApplicationResponseDto;
import org.ltt204.identityservice.presentations.web.mappers.AuthenticationMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    IAuthService authService;
    AuthenticationMapper authenticationMapper;

    @PostMapping("/signin")
    ResponseEntity<ApplicationResponseDto<AuthenticationResponseDto>> authenticate(@RequestBody UserSignInRequestDto requestDto) {
        var result = authService.authenticate(
                authenticationMapper.toSignInCredentialsDto(requestDto)
        );

        var responseBody = ApplicationResponseDto.success(authenticationMapper.toAuthenticationResponseDto(result));

        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/introspect")
    ResponseEntity<ApplicationResponseDto<?>> introspect(@RequestBody TokenIntrospectRequestDto introspectRequestDto) {
        var responseBody = IntrospectResponseDto.builder()
                .valid(authService.isValidToken(introspectRequestDto.getToken()))
                .build();

        return ResponseEntity.ok(
                ApplicationResponseDto.success(responseBody)
        );
    }

    @PostMapping("/revoke")
    ResponseEntity<ApplicationResponseDto<?>> revoke(@RequestBody RevokeTokenRequestDto requestDto) throws ParseException, JOSEException {
        return ResponseEntity.ok(
                ApplicationResponseDto.success(
                        authService.refreshToken(
                                authenticationMapper.toTokenPairDto(requestDto)
                        )
                )
        );
    }

    @PostMapping("/logout")
    ResponseEntity<ApplicationResponseDto<?>> logout(@RequestBody LogoutRequestDto requestDto) {
        authService.revokeToken(
                authenticationMapper.toTokenPairDto(requestDto)
        );
        
        return ResponseEntity.ok(ApplicationResponseDto.success());
    }
}
