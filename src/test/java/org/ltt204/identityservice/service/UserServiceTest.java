package org.ltt204.identityservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ltt204.identityservice.application.services.UserService;
import org.ltt204.identityservice.domain.entities.User;
import org.ltt204.identityservice.domain.events.UserCreatedEvent;
import org.ltt204.identityservice.domain.repositories.IUserRepository;
import org.ltt204.identityservice.infra.messaging.RabbitMQEventPublisher;
import org.ltt204.identityservice.infra.persist.jpa.repositories.JpaUserRepository;
import org.ltt204.identityservice.presentations.web.advices.AppException;
import org.ltt204.identityservice.presentations.web.advices.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(value = "/application-test.properties")
class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockitoBean
    private IUserRepository userRepository;
    @MockitoBean
    private JpaUserRepository jpaUserRepository;
    @MockitoBean
    private RabbitMQEventPublisher rabbitMQEventPublisher;

    private User userCreateRequestDto;
    private User user;

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
        when(userRepository.existsByName(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        doNothing().when(rabbitMQEventPublisher).publishEvent(UserCreatedEvent.builder().build());

        // WHEN
        var response = userService.create(userCreateRequestDto);

        // THEN
        var id = response.getId();
        assertThat(id).isEqualTo("randomStringThatRepresentTheUserId");
    }

    @Test
    void createUser_userExisted_fail() {
        // GIVEN
        when(userRepository.save(any())).thenThrow(AppException.class);

        // WHEN
        var exception = assertThrows(AppException.class, () -> userService.create(userCreateRequestDto));

        // THEN
        assertThat(exception.getError().getCode()).isEqualTo(ErrorCode.CONFLICT.getCode());
    }

    @Test
    void findUserByName_userExisted_Success() {
        when(userRepository.findFirstByUserName(anyString())).thenReturn(user);

        var response = userService.findFirstByUserName("john");

        var userId = response.getId();

        assertThat(userId).isEqualTo("randomStringThatRepresentTheUserId");
    }

    @Test
    void findUserByName_userNotExisted_fail() {
        when(userRepository.findFirstByUserName(anyString())).then(
                invocation -> {
                    throw new AppException(ErrorCode.NOT_FOUND.withErrorCode("User not found"));
                }
        );

        var exception = assertThrows(AppException.class, () -> userService.findFirstByUserName("Un-existed User"));

        assertThat(exception.getError().getCode()).isEqualTo(3001);
    }

    @Test
    void findUserById_userExisted_Success() {
        when(userRepository.findById(anyString())).thenReturn(user);

        var response = userService.findById("randomStringThatRepresentTheUserId");

        var userId = response.getId();

        assertThat(userId).isEqualTo("randomStringThatRepresentTheUserId");
    }

    @Test
    void findUserById_userNotExisted_fail() {
        when(userRepository.findById(anyString())).then(
                invocation -> {
                    throw new AppException(ErrorCode.NOT_FOUND.withErrorCode("User not found"));
                }
        );

        var exception = assertThrows(AppException.class, () -> userService.findById("Un-existed User"));

        assertThat(exception.getError().getCode()).isEqualTo(3001);
    }

    @Test
    void updateUser_userExisted_Success() {
        // GIVEN
        when(userRepository.findById(anyString())).thenReturn(user);
        when(userRepository.update(any())).thenReturn(user);

        // WHEN
        var response = userService.update(user);

        // THEN
        var userId = response.getId();

        assertThat(userId).isEqualTo("randomStringThatRepresentTheUserId");
    }

    @Test
    void updateUser_userNotExisted_fail() {
        // GIVEN
        when(userRepository.update(any())).thenThrow(AppException.class);

        // WHEN
        var exception = assertThrows(AppException.class, () -> userService.update(user));

        // THEN
        assertThat(exception.getError().getCode()).isEqualTo(ErrorCode.NOT_FOUND.getCode());
    }

    @Test
    void deleteUser_userExisted_Success() {
        // GIVEN
        when(userRepository.findById(anyString())).thenReturn(user);
        doNothing().when(userRepository).delete(any());

        // WHEN
        userService.delete("randomStringThatRepresentTheUserId");

        // THEN
        assertThat(true).isTrue();
    }
}