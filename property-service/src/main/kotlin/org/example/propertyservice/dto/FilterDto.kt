package org.example.propertyservice.dto

import org.example.propertyservice.document.Property
import java.math.BigDecimal

data class FilterDto(
    val title: String?,
    val type: Property.PropertyType?,
    val locationDto: LocationDto?,
    val startPricePerNight: BigDecimal?,
    val endPricePerNight: BigDecimal?,
    val free: Boolean?,
    var propertiesDtos: List<PropertyDto>?,
)
