package org.example.bookingservice.client

import org.example.bookingservice.dto.BankCardDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import reactor.core.publisher.Flux

@FeignClient(value = "bank-card")
interface BankCardClient {

    @GetMapping
    fun findAll(@RequestParam userId: String): List<BankCardDto>

    @PutMapping
    fun update(@RequestBody bankCardDto: BankCardDto)
}