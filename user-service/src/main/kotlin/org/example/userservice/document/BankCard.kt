package org.example.userservice.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.math.BigDecimal
import java.util.Date

@Document(collection = "bank_card")
class BankCard(
    @Id
    val id: String?,
    @Field(name = "user_id")
    val userId: String,
    val number: String,
    @Field(name = "expiry_date")
    val expiryDate: Date,
    @Field(name = "card_type")
    val cardType: CardType,
    val cvc: String,
    val balance: BigDecimal,
    @Field(name = "is_default")
    val priority: Boolean,
) {

    enum class CardType {
        VISA,
        MASTERCARD,
    }
}
