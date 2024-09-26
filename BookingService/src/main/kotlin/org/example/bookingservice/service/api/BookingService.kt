package org.example.bookingservice.service.api

import org.example.bookingservice.dto.BookingDto

interface BookingService {

    suspend fun findAllByTenantId(tenantId: String,) : List<BookingDto>

    suspend fun findById(bookingId: String,): BookingDto

    suspend fun create(bookingDto: BookingDto,): BookingDto

    suspend fun deleteById(bookingId: String,)
}