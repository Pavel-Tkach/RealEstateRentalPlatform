package org.example.bookingservice.service.api

import org.example.bookingservice.dto.PaymentDto

interface PaymentService {

    suspend fun findAllByTenantId(tenantId: String) : List<PaymentDto>

    suspend fun findById(bookingId: String, tenantId: String): PaymentDto

    suspend fun create(bookingId: String, paymentDto: PaymentDto): PaymentDto
}