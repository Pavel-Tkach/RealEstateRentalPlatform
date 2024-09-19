package org.example.bookingservice.dto

import org.bson.types.ObjectId
import org.example.bookingservice.document.enums.PaymentStatus
import java.math.BigDecimal
import java.time.Instant
import java.util.*

data class PaymentDto(

    val id: String? = ObjectId.get().toString(),

    val tenantId: String,

    val amount: BigDecimal,

    var status: PaymentStatus? = PaymentStatus.PENDING,

    val paymentDate: Date? = Date.from(Instant.now()),
)
