package org.ltt204.identityservice.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.ltt204.identityservice.dto.request.role.RoleCreateRequestDto;
import org.ltt204.identityservice.dto.request.role.RoleUpdateRequestDto;
import org.ltt204.identityservice.dto.response.role.RoleResponseDto;
import org.ltt204.identityservice.exception.AppException;
import org.ltt204.identityservice.exception.ErrorCode;
import org.ltt204.identityservice.mapper.RoleMapper;
import org.ltt204.identityservice.repository.PermissionRepository;
import org.ltt204.identityservice.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RoleService {
    PermissionRepository permissionRepository;

    RoleRepository roleRepository;
    RoleMapper roleMapper;

    public RoleResponseDto create(RoleCreateRequestDto roleCreateRequestDto) {
        var role = roleMapper.toRole(roleCreateRequestDto);
        //Map set<String> permissions of dto to set<Permission> of Role
        var permissions = permissionRepository.findAllByNameIn(roleCreateRequestDto.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        role = roleRepository.save(role);
        return roleMapper.toRoleDto(role);
    }

    public RoleResponseDto findById(int roleId) {
        var role = roleRepository.findById(roleId).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        return roleMapper.toRoleDto(role);
    }

    public List<RoleResponseDto> findAll() {
        var permissions = roleRepository.findAll();
        return permissions.stream().map(roleMapper::toRoleDto).toList();
    }

    public RoleResponseDto update(int roleId, RoleUpdateRequestDto roleUpdateRequestDto) {
        var permissions = permissionRepository.findAllByNameIn(
                roleUpdateRequestDto.getPermissions()
        );

        var role = roleRepository.findById(roleId).orElseThrow(() -> {
            var error = ErrorCode.NOT_FOUND;
            error.setMessage("Role not found");
            return new AppException(error);
        });

        roleMapper.updateRole(role, roleUpdateRequestDto);
        role.setPermissions(new HashSet<>(permissions));

        return roleMapper.toRoleDto(roleRepository.save(role));
    }

    public void delete(int roleId) {
        var role = roleRepository.findById(roleId).orElseThrow(() -> {
            var error = ErrorCode.NOT_FOUND;
            error.setMessage("Role not found");
            return new AppException(error);
        });
        roleRepository.delete(role);
    }
}
