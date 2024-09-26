package org.example.propertyservice.mapper

import org.example.propertyservice.document.Location
import org.example.propertyservice.dto.LocationDto
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface LocationMapper {

    fun toDto(location: Location): LocationDto

    fun toDocument(locationDto: LocationDto): Location
}
