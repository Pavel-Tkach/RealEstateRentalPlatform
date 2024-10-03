package org.example.bookingservice.exception

class BookingNotFoundException(
    override val message: String? = null
) : RuntimeException()