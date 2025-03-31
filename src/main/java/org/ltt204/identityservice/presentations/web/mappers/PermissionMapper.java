package org.ltt204.identityservice.presentations.web.mappers;

import org.ltt204.identityservice.domain.entities.Permission;
import org.ltt204.identityservice.presentations.web.dtos.responses.permission.PermissionResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(org.ltt204.identityservice.presentations.web.dtos.requests.permission.PermissionCreateRequestDto permissionCreateRequestDto);

    PermissionResponseDto toResponseDto(org.ltt204.identityservice.domain.entities.Permission permission);
}
