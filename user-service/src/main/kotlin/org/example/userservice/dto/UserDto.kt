package org.example.userservice.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

class UserDto(
    val id: String?,
    val firstname: String,
    val lastname: String,
    val email: String,
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    val password: String,
    val phone: String,
    val birthDate: Date,
)
