package org.example.userservice.dto

import org.example.userservice.entity.User
import java.time.OffsetDateTime
import java.util.Date

class UserUpdateDto(
    val id: String?,
    val firstname: String,
    val lastname: String,
    val email: String,
    val password: String,
    val phone: String,
    val birthDate: Date,
    val roles: List<User.RoleType> = listOf(),
)