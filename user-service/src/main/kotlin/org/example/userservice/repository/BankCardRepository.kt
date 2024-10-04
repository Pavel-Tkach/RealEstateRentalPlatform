package org.example.userservice.repository

import org.example.userservice.document.BankCard
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface BankCardRepository: CoroutineCrudRepository<BankCard, String> {

    suspend fun findAllByUserId(userId: String): List<BankCard>
}
