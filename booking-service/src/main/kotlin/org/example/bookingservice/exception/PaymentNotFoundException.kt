package org.example.bookingservice.exception

class PaymentNotFoundException(
    override val message: String? = null
) : RuntimeException()