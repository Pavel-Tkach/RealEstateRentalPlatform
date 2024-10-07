package org.example.bookingservice.exception

class PropertyAlreadyBookedException(
    override val message: String? = null
) : RuntimeException()

