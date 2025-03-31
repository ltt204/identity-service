package org.ltt204.identityservice.presentations.web.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.ltt204.identityservice.application.services.PermissionService;
import org.ltt204.identityservice.presentations.web.dtos.requests.permission.PermissionCreateRequestDto;
import org.ltt204.identityservice.presentations.web.dtos.responses.common.ApplicationResponseDto;
import org.ltt204.identityservice.presentations.web.dtos.responses.permission.PermissionResponseDto;
import org.ltt204.identityservice.presentations.web.mappers.PermissionMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/permissions")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;
    PermissionMapper permissionMapper;

    @PostMapping()
    ApplicationResponseDto<PermissionResponseDto> create(@RequestBody PermissionCreateRequestDto permissionCreateRequestDto) {
        var savedPermission = permissionService.save(
                permissionMapper.toPermission(permissionCreateRequestDto)
        );

        return ApplicationResponseDto.success(
                permissionMapper.toResponseDto(savedPermission)
        );
    }

    @GetMapping()
    ApplicationResponseDto<List<PermissionResponseDto>> getAll() {
        var permissions = permissionService.findAll();

        return ApplicationResponseDto.success(
                permissions.stream()
                        .map(permissionMapper::toResponseDto)
                        .toList()
        );
    }

    @GetMapping("/{permissionId}")
    ApplicationResponseDto<PermissionResponseDto> getById(@PathVariable int permissionId) {
        var permission = permissionService.findById(permissionId);
        return ApplicationResponseDto.success(
                permissionMapper.toResponseDto(permission)
        );
    }

    @DeleteMapping("/{permissionId}")
    ApplicationResponseDto<Void> delete(@PathVariable int permissionId) {
        permissionService.delete(permissionId);
        return ApplicationResponseDto.success("Successfully deleted");
    }

}
