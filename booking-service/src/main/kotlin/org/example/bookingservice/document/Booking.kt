package org.example.bookingservice.document

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.util.*

@Document(collection = "booking")
data class Booking(
    @Id
    val id: String? = ObjectId.get().toString(),
    val propertyId: String,
    val userId: String,
    val startDate: Date,
    val endDate: Date,
    var totalPrice: BigDecimal,
    var status: BookingStatus,
) {

    enum class BookingStatus {

        CONFIRMED,
        PENDING,
        CANCELLED,
        AWAIT_PAYMENT,
    }
}
