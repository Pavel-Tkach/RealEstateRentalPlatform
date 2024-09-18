package org.example.propertyservice.service.impl

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.example.propertyservice.dto.PropertyDto
import org.example.propertyservice.exception.PropertyNotFoundException
import org.example.propertyservice.mapper.PropertyMapper
import org.example.propertyservice.repository.PropertyRepository
import org.example.propertyservice.service.api.PropertyService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PropertyServiceImpl(
    private val propertyRepository: PropertyRepository,
    private val propertyMapper: PropertyMapper,
) : PropertyService {

    override suspend fun findAll(): List<PropertyDto> {
        return propertyRepository.findAll()
            .map { propertyMapper.toDto(it) }
            .toList()
    }

    override suspend fun findById(id: String,): PropertyDto {
        val property = (propertyRepository.findById(id)
            ?: throw PropertyNotFoundException("Property not found"))

        return propertyMapper.toDto(property)
    }

    @Transactional
    override suspend fun create(propertyDto: PropertyDto,): PropertyDto {
        val property = propertyMapper.toDocument(propertyDto)
        val savedProperty = propertyRepository.save(property)

        return propertyMapper.toDto(savedProperty)
    }

    @Transactional
    override suspend fun update(propertyDto: PropertyDto,) {
        val property = propertyMapper.toDocument(propertyDto)
        propertyRepository.save(property)
    }

    @Transactional
    override suspend fun deleteById(id: String,) {
        propertyRepository.deleteById(id)
    }
}