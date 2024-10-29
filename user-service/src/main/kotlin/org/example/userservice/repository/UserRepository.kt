package org.example.userservice.repository

import org.example.userservice.entity.User
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface UserRepository: R2dbcRepository<User, String> {

    fun findByEmail(email: String): Mono<User>
}
