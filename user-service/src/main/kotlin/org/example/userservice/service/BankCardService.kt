package org.example.userservice.service

import org.example.userservice.dto.BankCardDto
import org.example.userservice.exception.BankCardNotFoundException
import org.example.userservice.mapper.BankCardMapper
import org.example.userservice.repository.BankCardRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BankCardService(
    private val bankCardRepository: BankCardRepository,
    private val bankCardMapper: BankCardMapper,
) {

    suspend fun findAll(userId: String): List<BankCardDto> = bankCardRepository.findAllByUserId(userId)
        .map { bankCardMapper.toDto(it) }

    @Transactional
    suspend fun create(bankCardDto: BankCardDto): BankCardDto {
        val bankCard = bankCardMapper.toEntity(bankCardDto)
        val savedBankCard = bankCardRepository.save(bankCard)

        return bankCardMapper.toDto(savedBankCard)
    }

    @Transactional
    suspend fun update(bankCardDto: BankCardDto) {
        val bankCard = bankCardMapper.toEntity(bankCardDto)
        bankCardRepository.save(bankCard)
    }

    @Transactional
    suspend fun deleteById(bankCardId: String) {
        val bankCard = bankCardRepository.findById(bankCardId)
            ?: throw BankCardNotFoundException("Bank card with id $bankCardId not found")
        bankCardRepository.delete(bankCard)
    }
}
