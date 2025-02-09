package org.ltt204.identityservice.mapper;

import org.ltt204.identityservice.dto.request.role.RoleCreateRequestDto;
import org.ltt204.identityservice.dto.request.role.RoleUpdateRequestDto;
import org.ltt204.identityservice.dto.request.user.UserUpdateRequestDto;
import org.ltt204.identityservice.dto.response.role.RoleResponseDto;
import org.ltt204.identityservice.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.lang.annotation.Target;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleCreateRequestDto roleCreateRequestDto);
    RoleResponseDto toRoleDto(Role role);

    @Mapping(target ="permissions", ignore = true)
    void updateRole(@MappingTarget Role role, RoleUpdateRequestDto roleUpdateRequestDto);
}
