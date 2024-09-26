package org.example.propertyservice.dto

import org.bson.types.ObjectId
import org.example.propertyservice.document.Property
import java.math.BigDecimal

data class PropertyDto(
    val id: String? = ObjectId.get().toString(),
    val title: String,
    val description: String,
    val type: Property.PropertyType,
    val locationDto: LocationDto,
    val pricePerNight: BigDecimal,
    val ownerId: String,
    val free: Boolean,
)
