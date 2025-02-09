package org.ltt204.identityservice.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.ltt204.identityservice.dto.request.permission.PermissionCreateRequestDto;
import org.ltt204.identityservice.dto.response.permission.PermissionResponseDto;
import org.ltt204.identityservice.exception.AppException;
import org.ltt204.identityservice.exception.ErrorCode;
import org.ltt204.identityservice.mapper.PermissionMapper;
import org.ltt204.identityservice.repository.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponseDto create(PermissionCreateRequestDto permissionCreateRequestDto) {
        var permission = permissionMapper.toPermission(permissionCreateRequestDto);
        permissionRepository.save(permission);

        return permissionMapper.toPermissionDto(permission);
    }

    public PermissionResponseDto findById(int permissionId) {
        log.info("Permission Id {}", permissionId);
        var permission = permissionRepository.findById(permissionId).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        return permissionMapper.toPermissionDto(permission);
    }

    public List<PermissionResponseDto> findAll() {
        var permissions = permissionRepository.findAll();

        return permissions.stream().map(permissionMapper::toPermissionDto).toList();
    }

    public void delete(int permissionId) {
        var permission = permissionRepository.findById(permissionId).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        permissionRepository.delete(permission);
    }
}
