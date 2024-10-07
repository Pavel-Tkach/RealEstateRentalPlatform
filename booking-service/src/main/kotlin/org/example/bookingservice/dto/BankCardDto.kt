package org.example.bookingservice.dto

import java.math.BigDecimal
import java.util.Date

class BankCardDto(
    val id: String?,
    val userId: String,
    val number: String,
    val expiryDate: Date,
    val cardType: CardType,
    val cvc: String,
    var balance: BigDecimal,
    val priority: Boolean,
) {

    enum class CardType {
        VISA,
        MASTERCARD,
    }
}
