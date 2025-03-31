package org.ltt204.identityservice.application.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.ltt204.identityservice.application.dtos.user.UserDto;
import org.ltt204.identityservice.domain.entities.User;
import org.ltt204.identityservice.domain.repositories.IUserRepository;
import org.ltt204.identityservice.domain.services.IUserService;
import org.ltt204.identityservice.infra.persist.mappers.UserEntityMapper;
import org.ltt204.identityservice.presentations.web.advices.AppException;
import org.ltt204.identityservice.presentations.web.advices.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService implements IUserService {
    IUserRepository userRepository;
    UserEntityMapper userEntityMapper;
    PasswordEncoder passwordEncoder;

    @Override
    public UserDto create(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userEntityMapper.toDto(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(
                userEntityMapper::toDto
        );
    }

    @Override
    public UserDto findCurrentLoggedInUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Finding user with username: {}", username);
        return userEntityMapper.toDto(
                userRepository.findFirstByUserName(username)
        );
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    @PostAuthorize("returnObject.username == authentication.name")
    public UserDto findById(String userId) {
        return userEntityMapper.toDto(
                userRepository.findById(userId)
        );
    }

    @Override
    public UserDto findFirstByUserName(String name) {
        return userEntityMapper.toDto(
                userRepository.findFirstByUserName(name)
        );
    }

    @Override
    @PreAuthorize("hasAuthority('UPDATE_DATA')")
    @PostAuthorize("returnObject.username == authentication.name")
    public UserDto update(User user) {
        var currentUser = userRepository.findById(user.getId());

        userEntityMapper.toEntityFromUpdateRequest(currentUser, user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return userEntityMapper.toDto(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(String userId) {
        var user = userRepository.findById(userId);

        userRepository.delete(user);
    }

}
