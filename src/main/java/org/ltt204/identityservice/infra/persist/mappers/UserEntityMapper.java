package org.ltt204.identityservice.infra.persist.mappers;

import org.ltt204.identityservice.domain.entities.User;
import org.ltt204.identityservice.infra.persist.jpa.entities.JpaUser;
import org.ltt204.identityservice.application.dtos.user.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {
    User toDomainEntity (JpaUser jpaUser);

    JpaUser toJpaEntity (User user);

    UserDto toDto(User user);

    void toEntityFromUpdateRequest(@MappingTarget User user, User newUser);
}
