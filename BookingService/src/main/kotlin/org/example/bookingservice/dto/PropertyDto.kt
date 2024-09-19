package org.example.bookingservice.dto

import org.bson.types.ObjectId
import org.example.bookingservice.document.enums.PropertyType
import java.math.BigDecimal

data class PropertyDto(

    val id: String? = ObjectId.get().toString(),

    val title: String,

    val description: String,

    val type: PropertyType,

    val locationDto: LocationDto,

    val pricePerNight: BigDecimal,

    val ownerId: String,

    var free: Boolean
)