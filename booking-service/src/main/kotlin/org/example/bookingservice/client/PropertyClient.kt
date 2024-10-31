package org.example.bookingservice.client

import org.example.bookingservice.dto.PropertyDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import reactivefeign.spring.config.ReactiveFeignClient
import reactor.core.publisher.Mono

@ReactiveFeignClient(name = "property-service")
interface PropertyClient {

    @GetMapping("/properties/{propertyId}")
    fun findById(@PathVariable propertyId: String): Mono<PropertyDto>

    @PutMapping("/properties")
    fun update(@RequestBody propertyDto: PropertyDto): Mono<Void>
}
