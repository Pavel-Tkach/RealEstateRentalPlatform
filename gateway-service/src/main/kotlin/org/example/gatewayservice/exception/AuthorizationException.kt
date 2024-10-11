package org.example.gatewayservice.exception

class AuthorizationException(
    override val message: String? = null
) : RuntimeException()