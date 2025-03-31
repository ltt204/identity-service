package org.ltt204.identityservice.presentations.web.mappers;

import org.ltt204.identityservice.domain.entities.User;
import org.ltt204.identityservice.presentations.web.dtos.requests.user.UserCreateRequestDto;
import org.ltt204.identityservice.presentations.web.dtos.requests.user.UserUpdateRequestDto;
import org.ltt204.identityservice.application.dtos.user.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toDomainEntity(UserCreateRequestDto userCreateRequestDto);

    @Mapping(target = "roles", ignore = true)
    User toDomainEntity(UserUpdateRequestDto userUpdateRequestDto);

    UserDto toDto(User user);
}
