package org.example.bookingservice.dto

import org.example.bookingservice.document.Booking
import java.math.BigDecimal
import java.util.*

data class BookingDto(
    val id: String?,
    val propertyId: String,
    var userId: String?,
    val startDate: Date,
    val endDate: Date,
    val totalPrice: BigDecimal? = BigDecimal.ZERO,
    val status: Booking.BookingStatus? = Booking.BookingStatus.PENDING,
)
