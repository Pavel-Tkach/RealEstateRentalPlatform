package org.example.propertyservice.document

import org.bson.types.ObjectId
import org.example.propertyservice.document.enums.PropertyType
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal

@Document(collection = "property")
data class Property(

    @Id
    val id: String? = ObjectId.get().toString(),

    val title: String,

    val description: String,

    val type: PropertyType,

    val location: Location,

    val pricePerNight: BigDecimal,

    val ownerId: String,

    val free: Boolean,
)