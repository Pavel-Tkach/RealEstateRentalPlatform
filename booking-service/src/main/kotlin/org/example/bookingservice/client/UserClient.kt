package org.example.bookingservice.client

import org.example.bookingservice.dto.BankCardDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import reactivefeign.spring.config.ReactiveFeignClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@ReactiveFeignClient(value = "user-service")
interface UserClient {

    @GetMapping("/bankCards")
    fun findAll(@RequestParam userId: String): Flux<BankCardDto>

    @PutMapping("/bankCards")
    fun update(@RequestBody bankCardDto: BankCardDto): Mono<Void>
}
