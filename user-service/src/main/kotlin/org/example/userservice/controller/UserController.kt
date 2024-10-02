package org.example.userservice.controller

import org.example.userservice.dto.UserDto
import org.example.userservice.dto.UserUpdateDto
import org.example.userservice.service.UserService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService,
) {

    @GetMapping("/users")
    suspend fun findAll(): List<UserDto> = userService.findAll()

    @GetMapping("/users/search")
    suspend fun findByEmail(@RequestParam email: String): UserDto = userService.findByEmail(email)

    @PostMapping("/users")
    suspend fun create(@RequestBody userUpdateDto: UserUpdateDto,): UserUpdateDto = userService.create(userUpdateDto)

    @PutMapping("/users")
    suspend fun update(@RequestBody userUpdateDto: UserUpdateDto,): UserUpdateDto = userService.update(userUpdateDto)

    @DeleteMapping("/users/{userId}")
    suspend fun deleteById(@PathVariable userId: String,) = userService.deleteById(userId)
}