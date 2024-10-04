package org.example.userservice.mapper

import org.example.userservice.document.User
import org.example.userservice.dto.UserDto
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface UserMapper {

    fun toDto(user: User): UserDto

    fun toEntity(userDto: UserDto): User
}
