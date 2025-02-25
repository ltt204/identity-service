package org.ltt204.identityservice.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.ltt204.identityservice.dto.request.user.UserCreateRequestDto;
import org.ltt204.identityservice.dto.request.user.UserUpdateRequestDto;
import org.ltt204.identityservice.dto.response.user.UserDto;
import org.ltt204.identityservice.entity.Role;
import org.ltt204.identityservice.entity.User;
import org.ltt204.identityservice.exception.AppException;
import org.ltt204.identityservice.exception.ErrorCode;
import org.ltt204.identityservice.mapper.UserMapper;
import org.ltt204.identityservice.repository.RoleRepository;
import org.ltt204.identityservice.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserDto createUser(UserCreateRequestDto request) {
        if (userRepository.existsUsersByUsername(request.getUsername())) {
            var error = ErrorCode.CONFLICT;
            error.setMessage("Username is already taken");

            throw new AppException(error);
        }

        var user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        String DEFAULT_ROLE = "USER";
        roles.add(roleRepository.findByName(DEFAULT_ROLE));

        user.setRoles(roles);
        user = userRepository.save(user);
        log.info("userid: {}", user.getId());
        return userMapper.toUserDto(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserDto> findAll(Pageable pageable) {
        return userRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "userName"))
                )
        ).map(userMapper::toUserDto);
    }

    public UserDto getPersonalInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findFirstByUsername(username).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        return userMapper.toUserDto(user);
    }

    @PreAuthorize("hasRole('USER')")
    @PostAuthorize("returnObject.username == authentication.name")
    public UserDto findById(String userId) {
        SecurityContext contextHolder = SecurityContextHolder.getContext();
        Authentication authentication = contextHolder.getAuthentication();

        log.info("In method findById \n username: {} \n user principal: {}", authentication.getName(), authentication.getPrincipal());
        return userMapper.toUserDto(
                userRepository.findById(userId).orElseThrow(() -> {
                            var error = ErrorCode.NOT_FOUND;
                            error.setMessage("User is not existed");
                            return new AppException(error);
                        }
                )
        );
    }

    @PreAuthorize("hasAuthority('UPDATE_DATA')")
    @PostAuthorize("returnObject.username == authentication.name")
    public UserDto updateUser(String userId, UserUpdateRequestDto request) {
        var user = userRepository.findById(userId).orElseThrow(() -> {
                    var error = ErrorCode.NOT_FOUND;
                    error.setMessage("User is not existed");
                    return new AppException(error);
                }
        );

        HashSet<Role> roles = new HashSet<>();
        roles.add(
                roleRepository.findAllByNameIn(request.getRoles())
        );

        userMapper.updateUser(user, request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(roles);

        userRepository.save(user);
        return userMapper.toUserDto(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> {
            var error = ErrorCode.NOT_FOUND;
            error.setMessage("User is not existed");
            return new AppException(error);
        });
        userRepository.delete(user);
    }

}
