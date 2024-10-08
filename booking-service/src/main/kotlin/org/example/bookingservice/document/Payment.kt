package org.example.bookingservice.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.util.*

@Document(collection = "payment")
data class Payment(
    @Id
    val id: String?,
    val userId: String,
    val amount: BigDecimal?,
    var status: PaymentStatus,
    val paymentDate: Date,
) {

    enum class PaymentStatus {

        PAID,
        FAILED,
        PENDING,
    }
}
