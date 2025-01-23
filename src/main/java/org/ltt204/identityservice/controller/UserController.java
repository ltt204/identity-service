package org.ltt204.identityservice.controller;

import org.ltt204.identityservice.dto.request.UserCreateRequestDto;
import org.ltt204.identityservice.dto.request.UserUpdateRequestDto;
import org.ltt204.identityservice.entity.User;
import org.ltt204.identityservice.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    ResponseEntity<User> createUser(@RequestBody UserCreateRequestDto requestDto, UriComponentsBuilder ucb) {
        var createdUser = userService.createUser(requestDto);
        var locationOfNewUser = ucb.path("/users/{id}").buildAndExpand(createdUser.getId()).toUri();

        return ResponseEntity.created(locationOfNewUser).build();
    }

    @GetMapping("/{userId}")
    ResponseEntity<User> getUserById(@PathVariable String userId) {
        var user = userService.findById(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    ResponseEntity<Page<User>> getUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
    }

    @PutMapping("/{userId}")
    ResponseEntity<User> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequestDto requestDto) {
        var user = userService.updateUser(userId, requestDto);

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{userId}")
    ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
