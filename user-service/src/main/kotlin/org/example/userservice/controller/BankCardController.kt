package org.example.userservice.controller

import org.example.userservice.dto.BankCardDto
import org.example.userservice.service.BankCardService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
class BankCardController(
    private val bankCardService: BankCardService,
) {

    @GetMapping("/bankCards")
    fun findAll(@RequestParam userId: String): Flux<BankCardDto> = bankCardService.findAll(userId)

    @PostMapping("/bankCards")
    fun create(@RequestBody bankCardDto: BankCardDto,): Mono<BankCardDto> = bankCardService.create(bankCardDto)

    @PutMapping("/bankCards")
    fun update(@RequestBody bankCardDto: BankCardDto,): Mono<BankCardDto> = bankCardService.update(bankCardDto)

    @DeleteMapping("/bankCards/{bankCardId}")
    fun deleteById(@PathVariable bankCardId: Long,
                           @RequestParam userId: String,
                           ): Mono<Void> = bankCardService.deleteById(bankCardId, userId)
}
