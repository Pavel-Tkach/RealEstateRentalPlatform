package org.example.bookingservice.dto

import org.bson.types.ObjectId
import org.example.bookingservice.document.Payment
import java.math.BigDecimal
import java.time.Instant
import java.util.*

data class PaymentDto(
    val id: String? = ObjectId.get().toString(),
    val userId: String,
    var amount: BigDecimal?,
    var status: Payment.PaymentStatus? = Payment.PaymentStatus.PENDING,
    val paymentDate: Date? = Date.from(Instant.now()),
)
