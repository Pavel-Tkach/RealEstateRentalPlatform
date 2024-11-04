package org.example.userservice.service

import kotlinx.coroutines.reactive.awaitFirstOrElse
import kotlinx.coroutines.reactor.awaitSingle
import org.example.userservice.aspect.Loggable
import org.example.userservice.dto.UserDto
import org.example.userservice.dto.UserUpdateDto
import org.example.userservice.exception.UserNotFoundException
import org.example.userservice.mapper.UserMapper
import org.example.userservice.mapper.UserUpdateMapper
import org.example.userservice.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
    private val userUpdateMapper: UserUpdateMapper,
) {

    @Loggable
    fun findAll(): Flux<UserDto>  = userRepository.findAll()
        .map { userMapper.toDto(it) }

    @Loggable
    suspend fun findByEmail(email: String): Mono<UserDto> {
        val user = userRepository.findByEmail(email)
            .awaitFirstOrElse { throw UserNotFoundException("User with email $email not found") }

        return Mono.just(userMapper.toDto(user))
    }

    @Loggable
    @Transactional
    suspend fun update(userUpdateDto: UserUpdateDto,): Mono<UserUpdateDto> {
        val user = userUpdateMapper.toDocument(userUpdateDto)
        val savedUser = userRepository.save(user).awaitSingle()

        return Mono.just(userUpdateMapper.toDto(savedUser))
    }

    @Loggable
    @Transactional
    suspend fun deleteById(id: String,) = userRepository.deleteById(id)
}
