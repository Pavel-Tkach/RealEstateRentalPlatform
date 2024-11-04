package org.example.propertyservice.contoller

import org.example.propertyservice.aspect.Loggable
import org.example.propertyservice.dto.PropertyDto
import org.example.propertyservice.service.PropertyService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class PropertyController(
    private val propertyService: PropertyService,
) {

    @Loggable
    @GetMapping("/properties")
    suspend fun findAll(): List<PropertyDto> = propertyService.findAll()

    @Loggable
    @GetMapping("/properties/{propertyId}")
    suspend fun findById(@PathVariable propertyId: String,): PropertyDto = propertyService.findById(propertyId)

    @Loggable
    @PostMapping("/properties")
    suspend fun create(@RequestBody propertyDto: PropertyDto,
                       @RequestHeader("x-auth-user-id") userId: String,
                       ): PropertyDto {
        return propertyService.create(propertyDto, userId)
    }

    @Loggable
    @PutMapping("/properties")
    suspend fun update(@RequestBody propertyDto: PropertyDto,) = propertyService.update(propertyDto)

    @Loggable
    @DeleteMapping("/properties/{propertyId}")
    suspend fun deleteById(@PathVariable propertyId: String,
                           @RequestHeader("x-auth-user-id") userId: String, ) {
        propertyService.deleteById(propertyId, userId)
    }
}
