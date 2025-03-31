package org.ltt204.identityservice.infra.persist.mappers;

import org.ltt204.identityservice.application.dtos.role.RoleDto;
import org.ltt204.identityservice.domain.entities.Role;
import org.ltt204.identityservice.infra.persist.jpa.entities.JpaRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PermissionEntityMapper.class})
public interface RoleEntityMapper {
    @Mapping(target = "permissions", source = "jpaPermissions")
    Role toDomainEntity(JpaRole role);

    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "id", ignore = true)
    Role toDomainEntity(RoleDto requestDto);

    @Mapping(target = "jpaPermissions", source = "permissions")
    JpaRole toJpaEntity(Role role);
}
