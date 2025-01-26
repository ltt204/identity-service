package org.ltt204.identityservice.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.ltt204.identityservice.dto.request.UserCreateRequestDto;
import org.ltt204.identityservice.dto.request.UserUpdateRequestDto;
import org.ltt204.identityservice.dto.response.user.UserDto;
import org.ltt204.identityservice.exception.ErrorCode;
import org.ltt204.identityservice.exception.AppException;
import org.ltt204.identityservice.mapper.UserMapper;
import org.ltt204.identityservice.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService {
    UserRepository userRepository;
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

        userRepository.save(user);
        return userMapper.toUserDto(user);
    }

    public Page<UserDto> findAll(Pageable pageable) {
        return userRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "userName"))
                )
        ).map(userMapper::toUserDto);
    }

    public UserDto findById(String userId) {
        return userMapper.toUserDto(
                userRepository.findById(userId).orElseThrow(() -> {
                            var error = ErrorCode.NOT_FOUND;
                            error.setMessage("User is not existed");
                            return new AppException(error);
                        }
                )
        );
    }

    public UserDto updateUser(String userId, UserUpdateRequestDto request) {
        var user = userRepository.findById(userId).orElseThrow(() -> {
                    var error = ErrorCode.NOT_FOUND;
                    error.setMessage("User is not existed");
                    return new AppException(error);
                }
        );
        userMapper.updateUser(user, request);
        userRepository.save(user);
        return userMapper.toUserDto(user);
    }

    public void deleteUser(String userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> {
            var error = ErrorCode.NOT_FOUND;
            error.setMessage("User is not existed");
            return new AppException(error);
        });
        userRepository.delete(user);
    }
}
