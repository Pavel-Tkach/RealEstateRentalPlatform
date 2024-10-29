package org.example.userservice.service

import org.example.userservice.dto.BankCardDto
import org.example.userservice.exception.BankCardNotFoundException
import org.example.userservice.exception.IllegalRightsException
import org.example.userservice.mapper.BankCardMapper
import org.example.userservice.repository.BankCardRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class BankCardService(
    private val bankCardRepository: BankCardRepository,
    private val bankCardMapper: BankCardMapper,
) {

    fun findAll(userId: String): Flux<BankCardDto> = bankCardRepository.findAllByUserId(userId)
        .map { bankCardMapper.toDto(it) }

    @Transactional
    fun create(bankCardDto: BankCardDto): Mono<BankCardDto> {
        val bankCard = bankCardMapper.toDocument(bankCardDto)

        return bankCardRepository.save(bankCard)
            .map { bankCardMapper.toDto(it) }
    }

    @Transactional
    fun update(bankCardDto: BankCardDto): Mono<BankCardDto> {
        val bankCard = bankCardMapper.toDocument(bankCardDto)

        return bankCardRepository.save(bankCard)
            .map { bankCardMapper.toDto(it) }
    }

    @Transactional
    fun deleteById(bankCardId: Long, userId: String): Mono<Void> {
        return bankCardRepository.findById(bankCardId)
            .flatMap { bankCard ->
                validateOwnership(bankCard.userId, userId)
                    .then(bankCardRepository.delete(bankCard))
            }
            .onErrorResume { handleDeleteError(it, bankCardId) }
    }

    private fun validateOwnership(cardUserId: String, userId: String): Mono<Void> {
        return Mono.defer {
            if (cardUserId != userId) {
                Mono.error(IllegalRightsException("You can't remove this card, because you aren't its owner"))
            } else {
                Mono.empty()
            }
        }
    }

    private fun handleDeleteError(throwable: Throwable, bankCardId: Long): Mono<Void> {
        return when (throwable) {
            is BankCardNotFoundException -> Mono.error(BankCardNotFoundException("Bank card with id = $bankCardId not found"))
            else -> Mono.error(throwable)
        }
    }
}
