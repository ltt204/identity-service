package org.ltt204.identityservice.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.ltt204.identityservice.dto.request.UserSignInRequestDto;
import org.ltt204.identityservice.exception.customererror.ApplicationError;
import org.ltt204.identityservice.exception.customexception.ResourceNotFoundException;
import org.ltt204.identityservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public boolean signIn(UserSignInRequestDto requestDto) {
       var user = userRepository.findFirstByUsername(requestDto.getUsername()).orElseThrow(() ->
       {
           var error = ApplicationError.NOT_FOUND;
           error.setMessage("User not found");
           return new ResourceNotFoundException(error);
       });

       return passwordEncoder.matches(requestDto.getPassword(), user.getPassword());
    }
}
