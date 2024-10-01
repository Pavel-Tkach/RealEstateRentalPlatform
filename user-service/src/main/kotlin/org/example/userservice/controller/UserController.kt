package org.example.userservice.controller

import org.example.userservice.dto.UserDto
import org.example.userservice.service.UserService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.UUID

@RestController
class UserController(
    private val userService: UserService,
) {

    @GetMapping("/users")
    suspend fun findAll(): Mono<List<UserDto>> = userService.findAll()

    @GetMapping("/users/search")
    suspend fun findByEmail(@RequestParam email: String): Mono<UserDto> = userService.findByEmail(email)

    @PostMapping("/users")
    suspend fun create(@RequestBody userDto: UserDto,): Mono<UserDto> = userService.create(userDto)

    @PutMapping("/users")
    suspend fun update(@RequestBody userDto: UserDto,) = userService.update(userDto)

    @DeleteMapping("/users/{userId}")
    suspend fun deleteById(@PathVariable userId: UUID,) = userService.deleteById(userId)
}