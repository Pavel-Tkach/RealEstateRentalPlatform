package org.example.bookingservice.exception

class NotEnoughMoneyOnCardException(
    override val message: String? = null
) : RuntimeException()