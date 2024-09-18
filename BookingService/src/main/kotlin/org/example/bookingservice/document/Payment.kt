package org.example.bookingservice.document

import org.bson.types.ObjectId
import org.example.bookingservice.document.enums.PaymentStatus
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.util.*

@Document(collection = "payment")
data class Payment(

    @Id
    val id: String? = ObjectId.get().toString(),

    val tenantId: String,

    val amount: BigDecimal,

    var status: PaymentStatus,

    val paymentDate: Date,
)