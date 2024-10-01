package org.example.userservice.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime

@Table(name = "\"user\"")
class User(
    @Id
    val id: Long?,
    val firstname: String,
    val lastname: String,
    val email: String,
    val password: String,
    val phone: String,
    val birthDate: OffsetDateTime,
) {

    enum class RoleType {
        ROLE_TENANT,
        ROLE_LANDLORD,
        ROLE_ADMIN,
    }
}
