package org.example.userservice.dto

import org.example.userservice.document.User
import java.util.*

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