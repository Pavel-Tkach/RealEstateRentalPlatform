package org.example.propertyservice.util

import org.example.propertyservice.document.Location
import org.example.propertyservice.document.Property
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class DefaultDocumentCreator {

    fun createProperty(): Property {
        val location = Location("street Soviet 81C", "Grodno", "Grodno", "Belarus")

        return Property(
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