package org.example.userservice.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.OffsetDateTime

@Table("bank_card")
class BankCard(
    @Id
    val id: Long?,
    @Column("user_id")
    val userId: String,
    val number: String,
    @Column("expiry_date")
    val expiryDate: OffsetDateTime,
    @Column("card_type")
    val cardType: CardType,
    val cvc: String,
    val balance: BigDecimal,
    val priority: Boolean,
) {

    enum class CardType {
        VISA,
        MASTERCARD,
    }
}
