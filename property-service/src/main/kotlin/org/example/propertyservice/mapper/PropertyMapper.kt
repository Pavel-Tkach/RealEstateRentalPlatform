package org.example.propertyservice.mapper

import org.example.propertyservice.document.Property
import org.example.propertyservice.dto.PropertyDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring", uses = [LocationMapper::class])
interface PropertyMapper {

    @Mapping(source = "location", target = "locationDto")
    fun toDto(property: Property): PropertyDto

    @Mapping(source = "locationDto", target = "location")
    fun toDocument(propertyDto: PropertyDto): Property
}
