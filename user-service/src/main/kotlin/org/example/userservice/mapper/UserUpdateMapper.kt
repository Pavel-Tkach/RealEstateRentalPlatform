package org.example.userservice.mapper

import org.example.userservice.dto.UserUpdateDto
import org.example.userservice.entity.User
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface UserUpdateMapper {

    fun toDto(user: User): UserUpdateDto

    fun toEntity(userUpdateDto: UserUpdateDto): User
}