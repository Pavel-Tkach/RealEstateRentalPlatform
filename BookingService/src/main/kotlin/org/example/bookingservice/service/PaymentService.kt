package org.example.bookingservice.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.example.bookingservice.client.PropertyClient
import org.example.bookingservice.document.Booking
import org.example.bookingservice.document.Payment
import org.example.bookingservice.dto.PaymentDto
import org.example.bookingservice.exception.BookingNotFoundException
import org.example.bookingservice.exception.PaymentNotFoundException
import org.example.bookingservice.mapper.PaymentMapper
import org.example.bookingservice.repository.BookingRepository
import org.example.bookingservice.repository.PaymentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class PaymentService(
    private val bookingRepository: BookingRepository,
    private val paymentRepository: PaymentRepository,
    private val paymentMapper: PaymentMapper,
    private val propertyClient: PropertyClient,
) {

    suspend fun findAllByTenantId(tenantId: String): List<PaymentDto> {
        return paymentRepository.findAllByTenantId(tenantId)
            .map { paymentMapper.toDto(it) }
            .toList()
    }

    suspend fun findById(tenantId: String): PaymentDto {
        val payment = paymentRepository.findByTenantId(tenantId)
            ?: throw PaymentNotFoundException("Payment for user with id $tenantId not found")

        return paymentMapper.toDto(payment)
    }

    @Transactional
    suspend fun create(bookingId: String, paymentDto: PaymentDto): PaymentDto {
        val booking = (bookingRepository.findById(bookingId)
            ?: throw BookingNotFoundException("Booking with id $bookingId not found"))
        val change = paymentDto.amount - booking.totalPrice
        //todo сделать возврат сдачи пользователю
        if (change >= BigDecimal.ZERO) {
            paymentDto.status = Payment.PaymentStatus.PAID
            booking.status = Booking.BookingStatus.CONFIRMED
            bookingRepository.save(booking)
            val propertyDto = withContext(Dispatchers.IO) {
                propertyClient.findById(booking.propertyId)
            }
            propertyDto.free = false
            propertyClient.run { update(propertyDto) }
        } else {
            paymentDto.status = Payment.PaymentStatus.FAILED
            booking.status = Booking.BookingStatus.CANCELLED
            bookingRepository.save(booking)
        }
        val payment = paymentMapper.toDocument(paymentDto)
        val savedPayment = paymentRepository.save(payment)

        return paymentMapper.toDto(savedPayment)
    }
}
