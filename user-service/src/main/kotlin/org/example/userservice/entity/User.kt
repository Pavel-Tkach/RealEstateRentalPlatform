package org.example.userservice.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("user_entity")
class User(
    @Id
    val id: String?,
    @Column("first_name")
    val firstname: String,
    @Column("last_name")
    val lastname: String,
    val email: String,
)
