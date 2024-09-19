package org.example.bookingservice.repository

import org.example.bookingservice.document.Payment
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PaymentRepository: CoroutineCrudRepository<Payment, String> {

    suspend fun findAllByTenantId(tenantId: String): List<Payment>

    suspend fun findByTenantId(tenantId: String): Payment?
}