package org.example.propertyservice.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.example.propertyservice.document.Property
import java.math.BigDecimal

data class PropertyDto(
    val id: String?,
    @field:NotBlank(message = "Name cannot be empty")
    val title: String?,
    @field:NotBlank(message = "Description cannot be empty")
    val description: String?,
    @field:NotNull(message = "Type cannot be null")
    val type: Property.PropertyType?,
    @field:NotNull(message = "Location cannot be null")
    val locationDto: LocationDto?,
    @field:NotNull(message = "Price per night cannot be null")
    val pricePerNight: BigDecimal?,
    var ownerId: String?,
    val free: Boolean? = true,
)
