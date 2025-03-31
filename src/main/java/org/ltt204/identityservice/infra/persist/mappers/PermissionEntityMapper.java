package org.ltt204.identityservice.infra.persist.mappers;

import org.ltt204.identityservice.domain.entities.Permission;
import org.ltt204.identityservice.infra.persist.jpa.entities.JpaPermission;
import org.ltt204.identityservice.presentations.web.dtos.requests.permission.PermissionCreateRequestDto;
import org.ltt204.identityservice.presentations.web.dtos.responses.permission.PermissionResponseDto;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface PermissionEntityMapper {
    Permission toDomainEntity(JpaPermission jpaPermission);

    JpaPermission toJpaEntity(org.ltt204.identityservice.domain.entities.Permission permission);

    Set<Permission> toDomainEntities(Set<JpaPermission> jpaPermissions);

    Set<JpaPermission> toJpaEntities(Set<Permission> permissions);

    JpaPermission toJpaEntityFromCreateRequest(PermissionCreateRequestDto requestDto);

    Permission toEntityFromCreateRequest(PermissionCreateRequestDto requestDto);

    PermissionResponseDto toResponseDto(Permission permission);
}
