package org.example.bookingservice.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.withContext
import org.example.bookingservice.aspect.Loggable
import org.example.bookingservice.client.PropertyClient
import org.example.bookingservice.document.Booking
import org.example.bookingservice.dto.BookingDto
import org.example.bookingservice.exception.BookingNotFoundException
import org.example.bookingservice.exception.PropertyAlreadyBookedException
import org.example.bookingservice.mapper.BookingMapper
import org.example.bookingservice.repository.BookingRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.ZoneId
import java.time.temporal.ChronoUnit

@Service
class BookingService(
    private val bookingRepository: BookingRepository,
    private val bookingMapper: BookingMapper,
    private val propertyClient: PropertyClient,
    ) {

    @Loggable
    suspend fun findAllByUserId(userId: String,): List<BookingDto> {
        val bookingsByTenantId = bookingRepository.findAllByUserId(userId)

        return bookingsByTenantId
            .map { booking -> bookingMapper.toDto(booking) }
    }

    @Loggable
    suspend fun findById(bookingId: String,): BookingDto {
        val booking = (bookingRepository.findById(bookingId)
            ?: throw BookingNotFoundException("Booking with id $bookingId not found"))

        return bookingMapper.toDto(booking)
    }

    @Loggable
    @Transactional
    suspend fun create(bookingDto: BookingDto, userId: String,): BookingDto {
        bookingDto.userId = userId
        val propertyDto = withContext(Dispatchers.IO) {
            propertyClient.findById(bookingDto.propertyId)
        }.awaitSingle()
        if (!propertyDto.free) {
            throw PropertyAlreadyBookedException("Property ${bookingDto.propertyId} has already been booked")
        }
        val pricePerNight = propertyDto.pricePerNight
        val totalPrice = calculateTotalPriceForBooking(bookingDto, pricePerNight)
        val booking = bookingMapper.toDocument(bookingDto)
        booking.totalPrice = totalPrice
        booking.status = Booking.BookingStatus.AWAIT_PAYMENT
        val savedBooking = bookingRepository.save(booking)

        return bookingMapper.toDto(savedBooking)
    }

    @Loggable
    @Transactional
    suspend fun deleteById(bookingId: String,) {
        bookingRepository.deleteById(bookingId)
    }

    @Loggable
    private suspend fun calculateTotalPriceForBooking(bookingDto: BookingDto, pricePerNight: BigDecimal,): BigDecimal {
        val startDate = bookingDto.startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val endDate = bookingDto.endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val amountDays = ChronoUnit.DAYS.between(startDate, endDate)

        return pricePerNight.multiply(BigDecimal.valueOf(amountDays))
    }
}
