package org.ltt204.identityservice.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.ltt204.identityservice.dto.request.token.TokenIntrospectRequestDto;
import org.ltt204.identityservice.dto.request.user.UserSignInRequestDto;
import org.ltt204.identityservice.dto.response.common.ApplicationResponseDto;
import org.ltt204.identityservice.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthService authService;

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
}
