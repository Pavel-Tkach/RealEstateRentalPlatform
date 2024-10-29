package org.example.userservice.dto

import org.example.userservice.entity.BankCard.CardType
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.Date

class BankCardDto(
    val id: String?,
    val userId: String,
    val number: String,
    val expiryDate: OffsetDateTime,
    val cardType: CardType,
    val cvc: String,
    val balance: BigDecimal,
    val priority: Boolean,
)
