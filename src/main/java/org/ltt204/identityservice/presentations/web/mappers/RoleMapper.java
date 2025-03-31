package org.ltt204.identityservice.presentations.web.mappers;

import org.ltt204.identityservice.application.dtos.role.RoleDto;
import org.ltt204.identityservice.domain.entities.Role;
import org.ltt204.identityservice.presentations.web.dtos.requests.role.RoleCreateRequestDto;
import org.ltt204.identityservice.presentations.web.dtos.requests.role.RoleUpdateRequestDto;
import org.ltt204.identityservice.presentations.web.dtos.responses.role.RoleResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toDomainEntity(RoleCreateRequestDto requestDto);

    RoleDto toDto(RoleCreateRequestDto requestDto);

    RoleDto toDto(RoleUpdateRequestDto requestDto);

    @Mapping(target = "permissions", ignore = true)
    void updateDomainEntity(@MappingTarget Role role, RoleUpdateRequestDto responseDto);

    RoleResponseDto toResponseDto(Role role);
}
