package org.ltt204.identityservice.mapper;

import org.ltt204.identityservice.dto.request.UserCreateRequestDto;
import org.ltt204.identityservice.dto.request.UserUpdateRequestDto;
import org.ltt204.identityservice.dto.response.UserDto;
import org.ltt204.identityservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser (UserCreateRequestDto userCreateRequestDto);
    void updateUser (@MappingTarget User user, UserUpdateRequestDto userUpdateRequestDto);

    UserDto toUserDto(User user);
}
