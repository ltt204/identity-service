package org.ltt204.identityservice.controller;

import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.ltt204.identityservice.dto.request.auth.LogoutRequestDto;
import org.ltt204.identityservice.dto.request.auth.RevokeTokenRequestDto;
import org.ltt204.identityservice.dto.request.auth.TokenIntrospectRequestDto;
import org.ltt204.identityservice.dto.request.user.UserSignInRequestDto;
import org.ltt204.identityservice.dto.response.common.ApplicationResponseDto;
import org.ltt204.identityservice.service.AuthService;
import org.ltt204.identityservice.service.UserService;
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
    AuthService authService;
    UserService userService;

    @PostMapping("/signin")
    ResponseEntity<ApplicationResponseDto<?>> authenticate(@RequestBody UserSignInRequestDto requestDto) {
        return ResponseEntity.ok(
                ApplicationResponseDto.success(authService.authenticate(requestDto))
        );
    }

    @PostMapping("/introspect")
    ResponseEntity<ApplicationResponseDto<?>> introspect(@RequestBody TokenIntrospectRequestDto introspectRequestDto) {
        return ResponseEntity.ok(
                ApplicationResponseDto.success(
                        authService.introspect(introspectRequestDto)
                )
        );
    }

    @PostMapping("/revoke")
    ResponseEntity<ApplicationResponseDto<?>> revoke(@RequestBody RevokeTokenRequestDto requestDto) throws ParseException, JOSEException {
        return ResponseEntity.ok(
                ApplicationResponseDto.success(
                        authService.revokeToken(requestDto)
                )
        );
    }

    @PostMapping("/logout")
    ResponseEntity<ApplicationResponseDto<?>> logout(@RequestBody LogoutRequestDto requestDto) {
        authService.logout(requestDto);
        return ResponseEntity.ok(ApplicationResponseDto.success());
    }
}
