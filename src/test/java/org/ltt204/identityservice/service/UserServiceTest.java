package org.ltt204.identityservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ltt204.identityservice.dto.request.user.UserCreateRequestDto;
import org.ltt204.identityservice.entity.User;
import org.ltt204.identityservice.exception.AppException;
import org.ltt204.identityservice.repository.RoleRepository;
import org.ltt204.identityservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;
    @MockitoBean
    private RoleRepository roleRepository;

    private UserCreateRequestDto userCreateRequestDto;
    private User user;

    @BeforeEach
    void init() {
        var dob = LocalDate.of(1990, 1, 1);
        userCreateRequestDto = UserCreateRequestDto.builder()
                .username("john")
                .password("123456789")
                .firstName("john")
                .lastName("doe")
                .dateOfBirth(dob)
                .build();

        user = User.builder()
                .id("randomStringThatRepresentTheUserId")
                .username("john")
                .firstName("john")
                .lastName("doe")
                .dateOfBirth(dob)
                .build();
    }

    @Test
    void createUser_validRequest_success() {
        // GIVEN
        when(userRepository.existsUsersByUsername(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        // WHEN
        var response = userService.createUser(userCreateRequestDto);

        // THEN
        var id = response.getId();
        assertThat(id).isEqualTo("randomStringThatRepresentTheUserId");
    }

    @Test
    void createUser_userExisted_fail() {
        // GIVEN
        when(userRepository.existsUsersByUsername("john")).thenReturn(true);

        // WHEN
        var exception = assertThrows(AppException.class, () -> userService.createUser(userCreateRequestDto));

        // THEN
        assertThat(exception.getError().getCode()).isEqualTo(3000);
    }
}