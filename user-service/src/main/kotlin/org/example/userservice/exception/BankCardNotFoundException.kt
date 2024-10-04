package org.example.userservice.exception

class BankCardNotFoundException(
    override val message: String? = null
): RuntimeException()