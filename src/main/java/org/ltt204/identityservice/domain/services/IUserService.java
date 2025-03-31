package org.ltt204.identityservice.domain.services;

import org.ltt204.identityservice.application.dtos.user.UserDto;
import org.ltt204.identityservice.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {
    UserDto create(User user);

    UserDto update(User user);

    void delete(String userId);

    UserDto findById(String userId);

    UserDto findFirstByUserName(String name);

    Page<UserDto> findAll(Pageable pageable);

    UserDto findCurrentLoggedInUser();
}
