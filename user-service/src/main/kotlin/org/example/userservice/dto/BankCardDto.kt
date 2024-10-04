package org.example.userservice.dto

import org.example.userservice.document.BankCard.CardType
import java.math.BigDecimal
import java.util.Date

class BankCardDto(
    val id: String?,
    val userId: String,
    val number: String,
    val expiryDate: Date,
    val cardType: CardType,
    val cvc: String,
    val balance: BigDecimal,
)
