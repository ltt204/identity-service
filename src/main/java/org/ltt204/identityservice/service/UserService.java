package org.ltt204.identityservice.service;

import org.ltt204.identityservice.dto.request.UserCreateRequestDto;
import org.ltt204.identityservice.dto.request.UserUpdateRequestDto;
import org.ltt204.identityservice.entity.User;
import org.ltt204.identityservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(UserCreateRequestDto request) {
        var user =  new User();

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

    public Optional<User> findById(String userId) {
        return userRepository.findById(userId);
    }

    public User updateUser(String userId, UserUpdateRequestDto request) {
        var user = userRepository.getUserById(userId);

        user.setPassWord(request.getPassWord());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDateOfBirth(request.getDateOfBirth());

        userRepository.save(user);

        return user;
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
