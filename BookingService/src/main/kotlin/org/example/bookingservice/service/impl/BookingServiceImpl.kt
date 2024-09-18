package org.example.bookingservice.service.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.example.bookingservice.client.PropertyClient
import org.example.bookingservice.document.enums.BookingStatus
import org.example.bookingservice.dto.BookingDto
import org.example.bookingservice.exception.BookingNotFoundException
import org.example.bookingservice.mapper.BookingMapper
import org.example.bookingservice.repository.BookingRepository
import org.example.bookingservice.service.api.BookingService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.ZoneId
import java.time.temporal.ChronoUnit

@Service
class BookingServiceImpl(
    private val bookingRepository: BookingRepository,
    private val bookingMapper: BookingMapper,
    private val propertyClient: PropertyClient,
    ) : BookingService {

    override suspend fun findAll(tenantId: String): List<BookingDto> {
        val bookings = bookingRepository.findAllByTenantId(tenantId)

        return bookings
            .map { booking -> bookingMapper.toDto(booking) }
            .toList()
    }

    override suspend fun findAllByTenantId(tenantId: String): List<BookingDto> {
        val bookingsByTenantId = bookingRepository.findAllByTenantId(tenantId)

        return bookingsByTenantId
            .map { booking -> bookingMapper.toDto(booking) }
            .toList()
    }

    override suspend fun findById(bookingId: String): BookingDto {
        val booking = (bookingRepository.findById(bookingId)
            ?: throw BookingNotFoundException("Booking with id $bookingId not found"))

        return bookingMapper.toDto(booking)
    }

    @Transactional
    override suspend fun create(bookingDto: BookingDto): BookingDto {
        // todo оформление бронирования, подсчет цены и оформление ожидания оплаты
        val propertyDto = withContext(Dispatchers.IO) {
            propertyClient.findById(bookingDto.propertyId)
        }
        val pricePerNight = propertyDto.pricePerNight
        val totalPrice = calculateTotalPriceForBooking(bookingDto, pricePerNight)
        val booking = bookingMapper.toDocument(bookingDto)
        booking.totalPrice = totalPrice
        booking.status = BookingStatus.PENDING
        val savedBooking = bookingRepository.save(booking)

        return bookingMapper.toDto(savedBooking)
    }

    @Transactional
    override suspend fun deleteById(bookingId: String) {
        bookingRepository.deleteById(bookingId)
    }

    private suspend fun calculateTotalPriceForBooking(bookingDto: BookingDto, pricePerNight: BigDecimal): BigDecimal {
        val startDate = bookingDto.startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val endDate = bookingDto.endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val amountDays = ChronoUnit.DAYS.between(startDate, endDate)

        return pricePerNight.multiply(BigDecimal.valueOf(amountDays))
    }
}