package org.example.userservice.controller

import org.example.userservice.aspect.Loggable
import org.example.userservice.dto.BankCardDto
import org.example.userservice.service.BankCardService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
class BankCardController(
    private val bankCardService: BankCardService,
) {

    @Loggable
    @GetMapping("/bankCards")
    fun findAll(@RequestHeader("x-auth-user-id") userId: String): Flux<BankCardDto> = bankCardService.findAll(userId)

    @Loggable
    @PostMapping("/bankCards")
    fun create(@RequestBody bankCardDto: BankCardDto,
               @RequestHeader("x-auth-user-id") userId: String,
               ): Mono<BankCardDto> = bankCardService.create(bankCardDto, userId)

    @Loggable
    @PutMapping("/bankCards")
    fun update(@RequestBody bankCardDto: BankCardDto,): Mono<BankCardDto> = bankCardService.update(bankCardDto)

    @Loggable
    @DeleteMapping("/bankCards/{bankCardId}")
    fun deleteById(@PathVariable bankCardId: Long,
                   @RequestHeader("x-auth-user-id") userId: String,
                   ): Mono<Void> = bankCardService.deleteById(bankCardId, userId)
}
