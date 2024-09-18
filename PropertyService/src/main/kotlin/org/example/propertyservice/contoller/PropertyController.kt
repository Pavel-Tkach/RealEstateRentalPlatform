package org.example.propertyservice.contoller

import org.example.propertyservice.dto.PropertyDto
import org.example.propertyservice.service.api.PropertyService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/properties")
class PropertyController(
   private val propertyService: PropertyService,
) {

    @GetMapping
    suspend fun findAll(): List<PropertyDto> {
        return propertyService.findAll()
    }

    @GetMapping("/{propertyId}")
    suspend fun findById(@PathVariable propertyId: String,): PropertyDto {
        return propertyService.findById(propertyId)
    }

    @PostMapping
    suspend fun create(@RequestBody propertyDto: PropertyDto,): PropertyDto {
        return propertyService.create(propertyDto)
    }

    @PutMapping
    suspend fun update(@RequestBody propertyDto: PropertyDto,) {
        propertyService.update(propertyDto)
    }

    @DeleteMapping("/{propertyId}")
    suspend fun deleteById(@PathVariable propertyId: String,) {
        propertyService.deleteById(propertyId)
    }
}