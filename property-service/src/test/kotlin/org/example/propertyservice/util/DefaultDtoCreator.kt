package org.example.propertyservice.util

import org.example.propertyservice.document.Property
import org.example.propertyservice.dto.LocationDto
import org.example.propertyservice.dto.PropertyDto
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class DefaultDtoCreator {

    fun createPropertyDto(): PropertyDto {
        val location = LocationDto("street Soviet 81C", "Grodno", "Grodno", "Belarus")

        return PropertyDto(
            "67345edda795f93e969a08b6",
            "Home",
            "New Home",
            Property.PropertyType.HOUSE,
            location,
            BigDecimal.valueOf(400),
            "lfksalkf5aslkf2a",
            true
        )
    }
}
