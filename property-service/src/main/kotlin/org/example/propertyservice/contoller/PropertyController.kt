package org.example.propertyservice.contoller

import org.example.propertyservice.dto.PropertyDto
import org.example.propertyservice.service.PropertyService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class PropertyController(
    private val propertyService: PropertyService,
) {

    @GetMapping("/properties")
    suspend fun findAll(): List<PropertyDto> {
        return propertyService.findAll()
    }

    @GetMapping("/properties/{propertyId}")
    suspend fun findById(@PathVariable propertyId: String,): PropertyDto {
        return propertyService.findById(propertyId)
    }

    @PostMapping("/properties")
    suspend fun create(@RequestBody propertyDto: PropertyDto,): PropertyDto {
        return propertyService.create(propertyDto)
    }

    @PutMapping("/properties")
    suspend fun update(@RequestBody propertyDto: PropertyDto,) {
        propertyService.update(propertyDto)
    }

    @DeleteMapping("/properties/{propertyId}")
    suspend fun deleteById(@PathVariable propertyId: String, @RequestParam userId: String,) {
        propertyService.deleteById(propertyId, userId)
    }
}
