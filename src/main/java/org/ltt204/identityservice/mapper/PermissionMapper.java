package org.ltt204.identityservice.mapper;

import org.ltt204.identityservice.dto.request.permission.PermissionCreateRequestDto;
import org.ltt204.identityservice.dto.response.permission.PermissionResponseDto;
import org.ltt204.identityservice.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionCreateRequestDto permissionCreateRequestDto);

    PermissionResponseDto toPermissionDto(Permission permission);
}
