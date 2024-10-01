package org.example.userservice.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.OffsetDateTime

class UserDto(
    val id: Long?,
    val firstname: String,
    val lastname: String,
    val email: String,
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    val password: String,
    val phone: String,
    val birthDate: OffsetDateTime,
)
