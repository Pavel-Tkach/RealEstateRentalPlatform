package org.example.userservice.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.util.Date

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
)
