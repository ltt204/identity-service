package org.ltt204.identityservice.presentations.web.controllers;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.ltt204.identityservice.application.services.RoleService;
import org.ltt204.identityservice.application.services.UserService;
import org.ltt204.identityservice.presentations.web.dtos.requests.user.UserCreateRequestDto;
import org.ltt204.identityservice.presentations.web.dtos.requests.user.UserUpdateRequestDto;
import org.ltt204.identityservice.presentations.web.dtos.responses.common.ApplicationResponseDto;
import org.ltt204.identityservice.presentations.web.dtos.responses.common.ListResponse;
import org.ltt204.identityservice.presentations.web.dtos.responses.common.PageDto;
import org.ltt204.identityservice.application.dtos.user.UserDto;
import org.ltt204.identityservice.presentations.web.mappers.UserMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashSet;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    RoleService roleService;
    UserMapper userMapper;

    @PostMapping
    ResponseEntity<ApplicationResponseDto<UserDto>> createUser(@Valid @RequestBody UserCreateRequestDto requestDto, UriComponentsBuilder ucb) {
        var createdUser = userService.create(
                userMapper.toDomainEntity(requestDto)
        );

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

    @GetMapping("/info")
    ResponseEntity<ApplicationResponseDto<UserDto>> getPersonalInfo() {
        var user = userService.findCurrentLoggedInUser();

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
        var roles = roleService.findAllByNames(requestDto.getRoles().stream().toList());

        var user = userMapper.toDomainEntity(requestDto);
        user.setRoles(new HashSet<>(roles));
        user.setId(userId);

        return ResponseEntity.ok(ApplicationResponseDto.success(userService.update(user)));
    }

    @DeleteMapping("/{userId}")
    ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userService.delete(userId);

        return ResponseEntity.noContent().build();
    }
}
