package org.example.userservice.mapper

import org.example.userservice.document.User
import org.example.userservice.dto.UserUpdateDto
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface UserUpdateMapper {

    fun toDto(user: User): UserUpdateDto

    fun toDocument(userUpdateDto: UserUpdateDto): User
}