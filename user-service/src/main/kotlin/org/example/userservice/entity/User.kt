package org.example.userservice.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.util.*

@Document("user")
class User(
    @Id
    val id: String?,
    val firstname: String,
    val lastname: String,
    val email: String,
    val password: String,
    val phone: String,
    @Field(value = "birth_date")
    val birthDate: Date,
    var roles: List<RoleType> = emptyList(),
) {

    enum class RoleType {
        ROLE_USER,
        ROLE_ADMIN,
    }
}
