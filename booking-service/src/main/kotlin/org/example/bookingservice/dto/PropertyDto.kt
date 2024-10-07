package org.example.bookingservice.dto

import java.math.BigDecimal

data class PropertyDto(
    val id: String?,
    val title: String,
    val description: String,
    val type: PropertyType,
    val locationDto: LocationDto,
    val pricePerNight: BigDecimal,
    val ownerId: String,
    var free: Boolean,
) {

    enum class PropertyType {

        APARTMENT,
        HOUSE,
        OFFICE,
    }
}
