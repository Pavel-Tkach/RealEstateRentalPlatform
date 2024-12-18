package org.example.bookingservice.dto

import org.example.bookingservice.document.Payment
import java.math.BigDecimal
import java.time.Instant
import java.util.*

data class PaymentDto(
    val id: String?,
    var userId: String?,
    var amount: BigDecimal?,
    var status: Payment.PaymentStatus? = Payment.PaymentStatus.PENDING,
    val paymentDate: Date? = Date.from(Instant.now()),
)
