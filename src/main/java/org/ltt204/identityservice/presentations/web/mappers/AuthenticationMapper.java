package org.ltt204.identityservice.presentations.web.mappers;

import org.ltt204.identityservice.application.dtos.auth.AuthTokenDto;
import org.ltt204.identityservice.application.dtos.auth.SignInCredentialsDto;
import org.ltt204.identityservice.application.dtos.auth.TokenPairDto;
import org.ltt204.identityservice.presentations.web.dtos.requests.auth.LogoutRequestDto;
import org.ltt204.identityservice.presentations.web.dtos.requests.auth.RevokeTokenRequestDto;
import org.ltt204.identityservice.presentations.web.dtos.requests.user.UserSignInRequestDto;
import org.ltt204.identityservice.presentations.web.dtos.responses.auth.AuthenticationResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthenticationMapper {
    AuthenticationResponseDto toAuthenticationResponseDto(AuthTokenDto authTokenDto);

    AuthTokenDto toAuthenticationDto(AuthenticationResponseDto authenticationResponseDto);

    TokenPairDto toTokenPairDto(RevokeTokenRequestDto requestDto);

    TokenPairDto toTokenPairDto(LogoutRequestDto requestDto);

    SignInCredentialsDto toSignInCredentialsDto(UserSignInRequestDto userSignInRequestDto);
}
