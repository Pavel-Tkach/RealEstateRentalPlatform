package org.example.propertyservice.service

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.example.propertyservice.dto.PropertyDto
import org.example.propertyservice.exception.IllegalRightsException
import org.example.propertyservice.exception.PropertyNotFoundException
import org.example.propertyservice.mapper.PropertyMapper
import org.example.propertyservice.repository.PropertyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PropertyService(
    private val propertyRepository: PropertyRepository,
    private val propertyMapper: PropertyMapper,
) {

    suspend fun findAll(): List<PropertyDto> = propertyRepository.findAll()
        .map { propertyMapper.toDto(it) }
        .toList()

    suspend fun findById(id: String,): PropertyDto {
        val property = propertyRepository.findById(id)
            ?: throw PropertyNotFoundException("Property not found")

        return propertyMapper.toDto(property)
    }

    @Transactional
    suspend fun create(propertyDto: PropertyDto, userId: String,): PropertyDto {
        propertyDto.ownerId = userId
        val property = propertyMapper.toDocument(propertyDto)
        val savedProperty = propertyRepository.save(property)

        return propertyMapper.toDto(savedProperty)
    }

    @Transactional
    suspend fun update(propertyDto: PropertyDto,) {
        val property = propertyMapper.toDocument(propertyDto)
        propertyRepository.save(property)
    }

    @Transactional
    suspend fun deleteById(id: String, userId: String,) {
        val property = propertyRepository.findById(id)
            ?: throw PropertyNotFoundException("Property not found")
        if (property.ownerId != userId) {
            throw IllegalRightsException("You aren't owner of this property, so you cannot delete it.")
        }
        propertyRepository.deleteById(id)
    }
}
