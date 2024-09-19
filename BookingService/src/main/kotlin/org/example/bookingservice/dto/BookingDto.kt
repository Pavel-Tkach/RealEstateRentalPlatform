package org.example.bookingservice.dto

import org.bson.types.ObjectId
import org.example.bookingservice.document.enums.BookingStatus
import java.math.BigDecimal
import java.util.*

data class BookingDto(

    val id: String? = ObjectId.get().toString(),

    val propertyId: String,

    val tenantId: String,

    val startDate: Date,

    val endDate: Date,

    val totalPrice: BigDecimal? = BigDecimal.ZERO,

    val status: BookingStatus? = BookingStatus.PENDING,
)