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

@RestController
class BankCardController(
    private val bankCardService: BankCardService,
) {

    @GetMapping("/bankCards")
    suspend fun findAll(@RequestParam userId: String): List<BankCardDto> = bankCardService.findAll(userId)

    @PostMapping("/bankCards")
    suspend fun create(@RequestBody bankCardDto: BankCardDto,): BankCardDto = bankCardService.create(bankCardDto)

    @PutMapping("/bankCards")
    suspend fun update(@RequestBody bankCardDto: BankCardDto,) = bankCardService.update(bankCardDto)

    @DeleteMapping("/bankCards/{bankCardId}")
    suspend fun deleteById(@PathVariable bankCardId: String,) = bankCardService.deleteById(bankCardId)
}
