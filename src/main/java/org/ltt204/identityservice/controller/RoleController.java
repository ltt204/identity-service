package org.ltt204.identityservice.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.ltt204.identityservice.dto.request.role.RoleCreateRequestDto;
import org.ltt204.identityservice.dto.request.role.RoleUpdateRequestDto;
import org.ltt204.identityservice.dto.response.common.ApplicationResponseDto;
import org.ltt204.identityservice.dto.response.role.RoleResponseDto;
import org.ltt204.identityservice.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping()
    ResponseEntity<ApplicationResponseDto<RoleResponseDto>> create(@RequestBody RoleCreateRequestDto roleCreateRequestDto, UriComponentsBuilder ucb) {
        var roleResponseDto = roleService.create(roleCreateRequestDto);
        var responseBody = ApplicationResponseDto.success(roleResponseDto);
        var locationOfNewRole = ucb.path("/roles/{id}").buildAndExpand(roleResponseDto.getId()).toUri();

        return ResponseEntity.created(locationOfNewRole).body(responseBody);
    }

    @GetMapping
    ApplicationResponseDto<List<RoleResponseDto>> getAll() {
        return ApplicationResponseDto.success(roleService.findAll());
    }

    @GetMapping("/{roleId}")
    ApplicationResponseDto<RoleResponseDto> getById(@PathVariable int roleId) {
        return ApplicationResponseDto.success(
                roleService.findById(roleId)
        );
    }

    @DeleteMapping("/{roleId}")
    ApplicationResponseDto<RoleResponseDto> delete(@PathVariable int roleId) {
        roleService.delete(roleId);
        return ApplicationResponseDto.success();
    }

    @PutMapping("/{roleId}")
    ApplicationResponseDto<RoleResponseDto> update(@PathVariable int roleId, @RequestBody RoleUpdateRequestDto requestDto) {
        var role = roleService.update(roleId, requestDto);
        return ApplicationResponseDto.success(role);
    }
}
