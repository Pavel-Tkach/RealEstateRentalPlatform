package org.example.propertyservice.client

import org.example.propertyservice.dto.FilterDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import reactivefeign.spring.config.ReactiveFeignClient
import reactor.core.publisher.Mono

@ReactiveFeignClient(name = "recommendation-service", url = "http://recommendation-service:8000")
interface RecommendationClient {

    @PostMapping("/recommendations")
    fun getRecommendations(@RequestBody filterDto: FilterDto): Mono<FilterDto>
}