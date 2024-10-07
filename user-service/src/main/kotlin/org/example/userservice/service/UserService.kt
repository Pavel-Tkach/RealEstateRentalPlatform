package org.example.userservice.service

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.example.userservice.dto.UserDto
import org.example.userservice.dto.UserUpdateDto
import org.example.userservice.exception.UserNotFoundException
import org.example.userservice.mapper.UserMapper
import org.example.userservice.mapper.UserUpdateMapper
import org.example.userservice.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
    private val userUpdateMapper: UserUpdateMapper,
) {

    suspend fun findAll(): List<UserDto>  = userRepository.findAll()
        .map { userMapper.toDto(it) }
        .toList()

    suspend fun findByEmail(email: String): UserDto {
        val user = userRepository.findByEmail(email)
            ?: throw UserNotFoundException("User with email $email not found")

        return userMapper.toDto(user)
    }

    @Transactional
    suspend fun create(userUpdateDto: UserUpdateDto,): UserUpdateDto {
        val user = userUpdateMapper.toDocument(userUpdateDto)
        val savedUser = userRepository.save(user)

        return userUpdateMapper.toDto(savedUser)
    }

    @Transactional
    suspend fun update(userUpdateDto: UserUpdateDto,): UserUpdateDto {
        val user = userUpdateMapper.toDocument(userUpdateDto)
        val savedUser = userRepository.save(user)

        return userUpdateMapper.toDto(savedUser)
    }

    @Transactional
    suspend fun deleteById(id: String,) = userRepository.deleteById(id)
}
