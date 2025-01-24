package org.ltt204.identityservice.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.ltt204.identityservice.dto.request.UserCreateRequestDto;
import org.ltt204.identityservice.dto.request.UserUpdateRequestDto;
import org.ltt204.identityservice.dto.response.ApplicationResponseDto;
import org.ltt204.identityservice.dto.response.ListResponse;
import org.ltt204.identityservice.dto.response.PageDto;
import org.ltt204.identityservice.dto.response.UserDto;
import org.ltt204.identityservice.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping
    ResponseEntity<ApplicationResponseDto<UserDto>> createUser(@Valid @RequestBody UserCreateRequestDto requestDto, UriComponentsBuilder ucb) {
        var createdUser = userService.createUser(requestDto);

        var locationOfNewUser = ucb.path("/users/{id}").buildAndExpand(createdUser.getId()).toUri();
        var responseBody = ApplicationResponseDto.success(createdUser);
        return ResponseEntity.created(locationOfNewUser).body(responseBody);
    }

    @GetMapping("/{userId}")
    ResponseEntity<ApplicationResponseDto<UserDto>> getUserById(@PathVariable String userId) {
        var user = userService.findById(userId);
        var responseBody = ApplicationResponseDto.success(user);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping
    ResponseEntity<ApplicationResponseDto<ListResponse<UserDto>>> getUsers(Pageable pageable) {
        var pageResult = userService.findAll(pageable);

        var pageDto = PageDto.builder()
                .totalElements(pageResult.getTotalElements())
                .pageSize(pageResult.getSize())
                .pageNumber(pageResult.getNumber())
                .totalPages(pageResult.getTotalPages())
                .build();

        var listResponse = ListResponse.<UserDto>builder()
                .page(pageDto)
                .data(pageResult.get().toList())
                .build();

        return ResponseEntity.ok(ApplicationResponseDto.success(
                listResponse
        ));
    }

    @PutMapping("/{userId}")
    ResponseEntity<ApplicationResponseDto<UserDto>> updateUser(@PathVariable String userId, @RequestBody @Valid UserUpdateRequestDto requestDto) {
        var user = userService.updateUser(userId, requestDto);
        return ResponseEntity.ok(ApplicationResponseDto.success(user));
    }

    @DeleteMapping("/{userId}")
    ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
