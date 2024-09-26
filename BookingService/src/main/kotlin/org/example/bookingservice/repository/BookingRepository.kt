package org.example.bookingservice.repository

import org.example.bookingservice.document.Booking
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BookingRepository: CoroutineCrudRepository<Booking, String> {

    fun findAllByTenantId(tenantId: String) : List<Booking>
}
