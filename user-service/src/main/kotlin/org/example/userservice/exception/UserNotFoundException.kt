package org.example.userservice.exception

class UserNotFoundException(
    override val message: String? = null
): RuntimeException()