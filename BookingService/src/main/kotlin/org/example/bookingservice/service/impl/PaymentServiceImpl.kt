package org.example.bookingservice.service.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.example.bookingservice.client.PropertyClient
import org.example.bookingservice.document.enums.BookingStatus
import org.example.bookingservice.document.enums.PaymentStatus
import org.example.bookingservice.dto.PaymentDto
import org.example.bookingservice.exception.BookingNotFoundException
import org.example.bookingservice.exception.PaymentNotFoundException
import org.example.bookingservice.mapper.PaymentMapper
import org.example.bookingservice.repository.BookingRepository
import org.example.bookingservice.repository.PaymentRepository
import org.example.bookingservice.service.api.PaymentService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PaymentServiceImpl(
    private val bookingRepository: BookingRepository,
    private val paymentRepository: PaymentRepository,
    private val paymentMapper: PaymentMapper,
    private val propertyClient: PropertyClient,
) : PaymentService {

    override suspend fun findAllByTenantId(tenantId: String): List<PaymentDto> {
        return paymentRepository.findAllByTenantId(tenantId)
            .map { paymentMapper.toDto(it) }
            .toList()
    }

    override suspend fun findById(bookingId: String, tenantId: String): PaymentDto {
        val payment = paymentRepository.findByBookingIdAndTenantId(bookingId, tenantId)
            ?: throw PaymentNotFoundException("Payment for booking with id $bookingId not found")

        return paymentMapper.toDto(payment)
    }

    @Transactional
    override suspend fun create(bookingId: String, paymentDto: PaymentDto): PaymentDto {
        //todo если достаточно средств, то изменение статуса на PAID и подтверждение бронирования
        //todo обновление Property (устновка isFree = false)
        val booking = (bookingRepository.findById(bookingId)
            ?: throw BookingNotFoundException("Booking with id $bookingId not found"))
        if (booking.totalPrice <= paymentDto.amount) {
            //todo списание средств со счета арендатора
            paymentDto.status = PaymentStatus.PAID
            booking.status = BookingStatus.CONFIRMED
            bookingRepository.save(booking)
            val propertyDto = withContext(Dispatchers.IO) {
                propertyClient.findById(booking.propertyId)
            }
            propertyDto.isFree = false
            propertyClient.run { update(propertyDto) }
        }

        val payment = paymentMapper.toDocument(paymentDto)
        val savedPayment = paymentRepository.save(payment)

        return paymentMapper.toDto(savedPayment)
    }
}