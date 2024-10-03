package org.example.bookingservice.client

import org.example.bookingservice.dto.PropertyDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "property")
interface PropertyClient {

    @GetMapping("/{propertyId}")
    fun findById(@PathVariable propertyId: String): PropertyDto

    @PutMapping
    fun update(@RequestBody propertyDto: PropertyDto)
}
