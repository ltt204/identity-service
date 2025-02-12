package org.ltt204.identityservice.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.ltt204.identityservice.dto.request.permission.PermissionCreateRequestDto;
import org.ltt204.identityservice.dto.response.common.ApplicationResponseDto;
import org.ltt204.identityservice.dto.response.permission.PermissionResponseDto;
import org.ltt204.identityservice.service.PermissionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/permissions")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @PostMapping()
    ApplicationResponseDto<PermissionResponseDto> create(@RequestBody PermissionCreateRequestDto permissionCreateRequestDto) {
        var responseDto = permissionService.create(permissionCreateRequestDto);
        return ApplicationResponseDto.success(responseDto);
    }

    @GetMapping()
    ApplicationResponseDto<List<PermissionResponseDto>> getAll() {
        var responseDto = permissionService.findAll();
        return ApplicationResponseDto.success(responseDto);
    }

    @GetMapping("/{permissionId}")
    ApplicationResponseDto<PermissionResponseDto> getById(@PathVariable int permissionId) {
        var responseDto = permissionService.findById(permissionId);
        return ApplicationResponseDto.success(responseDto);
    }

    @DeleteMapping("/{permissionId}")
    ApplicationResponseDto<Void> delete(@PathVariable int permissionId) {
        permissionService.delete(permissionId);
        return ApplicationResponseDto.success("Successfully deleted");
    }

}
