package org.ltt204.identityservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ltt204.identityservice.application.services.UserService;
import org.ltt204.identityservice.domain.entities.User;
import org.ltt204.identityservice.infra.persist.jpa.entities.JpaUser;
import org.ltt204.identityservice.presentations.web.dtos.requests.user.UserCreateRequestDto;
import org.ltt204.identityservice.presentations.web.advices.AppException;
import org.ltt204.identityservice.infra.persist.jpa.repositories.JpaRoleRepository;
import org.ltt204.identityservice.infra.persist.jpa.repositories.JpaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class JpaUserServiceTest {
    @Autowired
    private UserService userService;

    @MockitoBean
    private JpaUserRepository userRepository;
    @MockitoBean
    private JpaRoleRepository jpaRoleRepository;

    private User userCreateRequestDto;
    private JpaUser user;

    @BeforeEach
    void init() {
        var dob = LocalDate.of(1990, 1, 1);
        userCreateRequestDto = User.builder()
                .username("john")
                .password("123456789")
                .firstName("john")
                .lastName("doe")
                .dateOfBirth(dob)
                .build();

        user = JpaUser.builder()
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
        var response = userService.create(userCreateRequestDto);

        // THEN
        var id = response.getId();
        assertThat(id).isEqualTo("randomStringThatRepresentTheUserId");
    }

    @Test
    void createUser_userExisted_fail() {
        // GIVEN
        when(userRepository.existsUsersByUsername("john")).thenReturn(true);

        // WHEN
        var exception = assertThrows(AppException.class, () -> userService.create(userCreateRequestDto));

        // THEN
        assertThat(exception.getError().getCode()).isEqualTo(3000);
    }
}