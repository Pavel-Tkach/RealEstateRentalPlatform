package org.example.propertyservice.service.api

import org.example.propertyservice.dto.PropertyDto

interface PropertyService {

    suspend fun findAll(): List<PropertyDto>

    suspend fun findById(id: String,): PropertyDto

    suspend fun create(propertyDto: PropertyDto,) : PropertyDto

    suspend fun update(propertyDto: PropertyDto,)

    suspend fun deleteById(id: String,)
}