package org.example.userservice.service

import kotlinx.coroutines.reactor.awaitSingle
import org.example.userservice.dto.UserDto
import org.example.userservice.exception.UserNotFoundException
import org.example.userservice.mapper.UserMapper
import org.example.userservice.repository.UserRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
) {

    suspend fun findAll(): Mono<List<UserDto>>  = userRepository.findAll()
        .map { userMapper.toDto(it) }
        .collectList()

    suspend fun findByEmail(email: String): Mono<UserDto> {
        val user = userRepository.findByEmail(email).awaitSingle()
        val userDto = userMapper.toDto(user)

        return Mono.just(userDto)
    }

    suspend fun create(userDto: UserDto,): Mono<UserDto> {
        val user = userMapper.toEntity(userDto)
        val savedUser = userRepository.save(user)

        return Mono.just(userMapper.toDto(savedUser.awaitSingle()))
    }

    suspend fun update(userDto: UserDto,): Mono<UserDto> {
        val user = userMapper.toEntity(userDto)
        val savedUser = userRepository.save(user)

        return Mono.just(userMapper.toDto(savedUser.awaitSingle()))
    }

    suspend fun deleteById(id: UUID,) = userRepository.deleteById(id)
}