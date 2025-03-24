package org.example.propertyservice.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.withContext
import org.example.propertyservice.aspect.Loggable
import org.example.propertyservice.client.RecommendationClient
import org.example.propertyservice.dto.FilterDto
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
    private val recommendationClient: RecommendationClient,
) {

    @Loggable
    suspend fun findAll(): List<PropertyDto> = propertyRepository.findAll()
        .map { propertyMapper.toDto(it) }
        .toList()

    @Loggable
    suspend fun getRecommendations(filterDto: FilterDto): List<PropertyDto> {
        val properties = propertyRepository.findAll()
        val propertiesDto = properties
            .map { propertyMapper.toDto(it) }
            .toList()
        filterDto.propertiesDtos = propertiesDto

        val dto = withContext(Dispatchers.IO) {
            recommendationClient.getRecommendations(filterDto)
                .awaitSingle()
        }

        return dto.propertiesDtos!!
    }

    @Loggable
    suspend fun findById(id: String,): PropertyDto {
        val property = propertyRepository.findById(id)
            ?: throw PropertyNotFoundException("Property not found")

        return propertyMapper.toDto(property)
    }

    @Loggable
    @Transactional
    suspend fun create(propertyDto: PropertyDto, userId: String,): PropertyDto {
        propertyDto.ownerId = userId
        val property = propertyMapper.toDocument(propertyDto)
        val savedProperty = propertyRepository.save(property)

        return propertyMapper.toDto(savedProperty)
    }

    @Loggable
    @Transactional
    suspend fun update(propertyDto: PropertyDto,) {
        val property = propertyMapper.toDocument(propertyDto)
        propertyRepository.save(property)
    }

    @Loggable
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
