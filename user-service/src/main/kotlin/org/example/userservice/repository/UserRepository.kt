package org.example.userservice.repository

import org.example.userservice.entity.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: CoroutineCrudRepository<User, String> {

    suspend fun findByEmail(email: String): User
}
