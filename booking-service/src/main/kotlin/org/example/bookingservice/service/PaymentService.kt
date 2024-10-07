package org.example.bookingservice.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.withContext
import org.example.bookingservice.client.BankCardClient
import org.example.bookingservice.client.PropertyClient
import org.example.bookingservice.document.Booking
import org.example.bookingservice.document.Payment
import org.example.bookingservice.dto.PaymentDto
import org.example.bookingservice.exception.BookingNotFoundException
import org.example.bookingservice.exception.NotEnoughMoneyOnCardException
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
    private val bankCardClient: BankCardClient,
) {

    suspend fun findAllByUserId(userId: String): List<PaymentDto> {
        return paymentRepository.findAllByUserId(userId)
            .map { paymentMapper.toDto(it) }
    }

    suspend fun findById(userId: String): PaymentDto {
        val payment = paymentRepository.findByUserId(userId)
            ?: throw PaymentNotFoundException("Payment for user with id $userId not found")

        return paymentMapper.toDto(payment)
    }

    @Transactional
    suspend fun create(bookingId: String, paymentDto: PaymentDto): PaymentDto {
        val booking = (bookingRepository.findById(bookingId)
            ?: throw BookingNotFoundException("Booking with id $bookingId not found"))
        val bankCards = withContext(Dispatchers.IO) {
            bankCardClient.findAll(paymentDto.userId)
        }
        val priorityBankCard = bankCards.first { card -> card.priority }
        val balance = priorityBankCard.balance
        //todo списание со счета сердств за бронирование, установка в поле amount суммы бронирования, которая была уже списана
        if (booking.totalPrice <= balance) {
            priorityBankCard.balance -= booking.totalPrice
            bankCardClient.run { update(priorityBankCard) }
            paymentDto.status = Payment.PaymentStatus.PAID
            booking.status = Booking.BookingStatus.CONFIRMED
            bookingRepository.save(booking)
            val propertyDto = withContext(Dispatchers.IO) {
                propertyClient.findById(booking.propertyId)
            }
            propertyDto.free = false
            propertyClient.run { update(propertyDto) }
        }
        else {
            paymentDto.status = Payment.PaymentStatus.FAILED
            booking.status = Booking.BookingStatus.CANCELLED
            bookingRepository.save(booking)
            throw NotEnoughMoneyOnCardException("Not enough money on your priority card")
        }
        paymentDto.amount = booking.totalPrice
        val payment = paymentMapper.toDocument(paymentDto)
        val savedPayment = paymentRepository.save(payment)

        return paymentMapper.toDto(savedPayment)
    }
}
