package org.ltt204.identityservice.presentations.web.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.ltt204.identityservice.application.services.RoleService;
import org.ltt204.identityservice.presentations.web.dtos.requests.role.RoleCreateRequestDto;
import org.ltt204.identityservice.presentations.web.dtos.requests.role.RoleUpdateRequestDto;
import org.ltt204.identityservice.presentations.web.dtos.responses.common.ApplicationResponseDto;
import org.ltt204.identityservice.presentations.web.dtos.responses.role.RoleResponseDto;
import org.ltt204.identityservice.presentations.web.mappers.RoleMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;
    RoleMapper roleMapper;

    @PostMapping()
    ResponseEntity<ApplicationResponseDto<RoleResponseDto>> create(@RequestBody RoleCreateRequestDto roleCreateRequestDto, UriComponentsBuilder ucb) {
        log.info("Creating role with permissions {}", roleCreateRequestDto.getPermissions());
        var role = roleService.save(
                roleMapper.toDto(roleCreateRequestDto)
        );
        log.info("Created role with id {}", role.getId());

        var responseBody = ApplicationResponseDto.success(
                roleMapper.toResponseDto(role)
        );

        var locationOfNewRole = ucb.path("/roles/{id}").buildAndExpand(role.getId()).toUri();

        return ResponseEntity.created(locationOfNewRole).body(responseBody);
    }

    @GetMapping
    ApplicationResponseDto<List<RoleResponseDto>> getAll() {
        var roles = roleService.findAll();

        return ApplicationResponseDto.success(
                roles.stream()
                        .map(roleMapper::toResponseDto)
                        .toList()
        );
    }

    @GetMapping("/{roleId}")
    ApplicationResponseDto<RoleResponseDto> getById(@PathVariable int roleId) {
        var role = roleService.findById(roleId);

        return ApplicationResponseDto.success(
                roleMapper.toResponseDto(role)
        );
    }

    @DeleteMapping("/{roleId}")
    ApplicationResponseDto<RoleResponseDto> delete(@PathVariable int roleId) {
        roleService.delete(roleId);

        return ApplicationResponseDto.success();
    }

    @PutMapping("/{roleId}")
    ApplicationResponseDto<RoleResponseDto> update(@PathVariable int roleId, @RequestBody RoleUpdateRequestDto requestDto) {
        var updatedRole = roleService.update(
                roleId,
                roleMapper.toDto(requestDto)
        );

        return ApplicationResponseDto.success(
                roleMapper.toResponseDto(updatedRole)
        );
    }
}
