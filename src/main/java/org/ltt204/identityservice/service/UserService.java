package org.ltt204.identityservice.service;

import org.ltt204.identityservice.dto.request.UserCreateRequestDto;
import org.ltt204.identityservice.dto.request.UserUpdateRequestDto;
import org.ltt204.identityservice.dto.response.ApplicationPaginationResponseDto;
import org.ltt204.identityservice.entity.User;
import org.ltt204.identityservice.exception.customererror.ApplicationError;
import org.ltt204.identityservice.exception.customexception.ResourceConflictException;
import org.ltt204.identityservice.exception.customexception.ResourceNotFoundException;
import org.ltt204.identityservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(UserCreateRequestDto request) {
        var user = new User();

        if (userRepository.existsUsersByUserName(request.getUserName())) {
            var error = ApplicationError.CONFLICT;
            error.setMessage("Username is already taken");

            throw new ResourceConflictException(error);
        }

        user.setUserName(request.getUserName());
        user.setPassWord(request.getPassWord());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDateOfBirth(request.getDateOfBirth());

        userRepository.save(user);

        return user;
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "userName"))
                )
        );
    }

    public User findById(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            var error = ApplicationError.NOT_FOUND;
            error.setMessage("User is not existed");
            return new ResourceNotFoundException(error);
        });
    }

    public User updateUser(String userId, UserUpdateRequestDto request) {
        var user = userRepository.findById(userId).orElseThrow(() -> {
                    var error = ApplicationError.NOT_FOUND;
                    error.setMessage("User is not existed");
                    return new ResourceNotFoundException(error);
                }
        );

        user.setPassWord(request.getPassWord());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDateOfBirth(request.getDateOfBirth());

        userRepository.save(user);

        return user;
    }

    public void deleteUser(String userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> {
            var error = ApplicationError.NOT_FOUND;
            error.setMessage("User is not existed");
            return new ResourceNotFoundException(error);
        });
        userRepository.delete(user);
    }
}
