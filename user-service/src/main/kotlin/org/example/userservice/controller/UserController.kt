package org.example.userservice.controller

import org.example.userservice.aspect.Loggable
import org.example.userservice.dto.UserDto
import org.example.userservice.dto.UserUpdateDto
import org.example.userservice.service.UserService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
class UserController(
    private val userService: UserService,
) {

    @Loggable
    @GetMapping("/users")
    suspend fun findAll(): Flux<UserDto> = userService.findAll()

    @Loggable
    @GetMapping("/users/search")
    suspend fun findByEmail(@RequestParam email: String): Mono<UserDto> = userService.findByEmail(email)

    @Loggable
    @PutMapping("/users")
    suspend fun update(@RequestBody userUpdateDto: UserUpdateDto,): Mono<UserUpdateDto> = userService.update(userUpdateDto)

    @Loggable
    @DeleteMapping("/users/{userId}")
    suspend fun deleteById(@PathVariable userId: String,) = userService.deleteById(userId)
}
