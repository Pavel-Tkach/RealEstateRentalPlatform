package org.example.userservice.repository

import org.example.userservice.entity.BankCard
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface BankCardRepository: R2dbcRepository<BankCard, Long> {

    fun findAllByUserId(userId: String): Flux<BankCard>
}
