package org.ltt204.identityservice.mapper;

import org.ltt204.identityservice.dto.request.user.UserCreateRequestDto;
import org.ltt204.identityservice.dto.request.user.UserUpdateRequestDto;
import org.ltt204.identityservice.dto.response.user.UserDto;
import org.ltt204.identityservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreateRequestDto userCreateRequestDto);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequestDto userUpdateRequestDto);

    UserDto toUserDto(User user);
}
