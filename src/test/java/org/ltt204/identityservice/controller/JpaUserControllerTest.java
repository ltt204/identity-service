package org.ltt204.identityservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ltt204.identityservice.application.dtos.user.UserDto;
import org.ltt204.identityservice.application.services.UserService;
import org.ltt204.identityservice.domain.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class JpaUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private User userCreateRequestDto;
    private UserDto userDto;

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

        userDto = UserDto.builder()
                .id("randomStringThatRepresentTheUserId")
                .username("john")
                .firstName("john")
                .lastName("doe")
                .dateOfBirth(dob)
                .build();
    }

    @Test
    public void createUser_validRequest_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String content = objectMapper.writeValueAsString(userCreateRequestDto);

        when(userService.create(any())).thenReturn(
                userDto
        );

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("code").value(1000)
                );
    }

    @Test
    public void createUser_invalidUsername_success() throws Exception {
        // GIVEN
        userCreateRequestDto.setUsername("jo");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String content = objectMapper.writeValueAsString(userCreateRequestDto);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(2002)
                );
    }

    @Test
    public void createUser_invalidPassword_success() throws Exception {
        // GIVEN
        userCreateRequestDto.setPassword("123");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String content = objectMapper.writeValueAsString(userCreateRequestDto);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(2003)
                );
    }

    @Test
    public void createUser_invalidDateOfBirth_success() throws Exception {
        // GIVEN
        userCreateRequestDto.setDateOfBirth(LocalDate.now());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String content = objectMapper.writeValueAsString(userCreateRequestDto);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(2004)
                );
    }
}