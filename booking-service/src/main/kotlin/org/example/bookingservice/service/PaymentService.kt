package org.example.bookingservice.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.withContext
import org.example.bookingservice.client.BankCardClient
import org.example.bookingservice.client.PropertyClient
import org.example.bookingservice.document.Booking
import org.example.bookingservice.document.Payment
import org.example.bookingservice.dto.BankCardDto
import org.example.bookingservice.dto.PaymentDto
import org.example.bookingservice.exception.BookingNotFoundException
import org.example.bookingservice.exception.NotEnoughMoneyOnCardException
import org.example.bookingservice.exception.PaymentNotFoundException
import org.example.bookingservice.mapper.PaymentMapper
import org.example.bookingservice.repository.BookingRepository
import org.example.bookingservice.repository.PaymentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PaymentService(
    private val bookingRepository: BookingRepository,
    private val paymentRepository: PaymentRepository,
    private val paymentMapper: PaymentMapper,
    private val propertyClient: PropertyClient,
    private val bankCardClient: BankCardClient,
) {

    suspend fun findAllByUserId(userId: String): List<PaymentDto> = paymentRepository.findAllByUserId(userId)
            .map { paymentMapper.toDto(it) }

    suspend fun findById(userId: String): PaymentDto {
        val payment = paymentRepository.findByUserId(userId)
            ?: throw PaymentNotFoundException("Payment for user with id $userId not found")

        return paymentMapper.toDto(payment)
    }

    @Transactional
    suspend fun create(bookingId: String, paymentDto: PaymentDto, userId: String,): PaymentDto {
        paymentDto.userId = userId
        val booking = (bookingRepository.findById(bookingId)
            ?: throw BookingNotFoundException("Booking with id $bookingId not found"))
        val priorityBankCard = getPriorityBankCard(paymentDto)
        val balance = priorityBankCard.balance
        if (booking.totalPrice <= balance) {
            executePaymentAndUpdateBookingStatus(priorityBankCard, booking, paymentDto)
            val propertyDto = withContext(Dispatchers.IO) {
                propertyClient.findById(booking.propertyId)
            }.awaitSingle()
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

    suspend fun getPriorityBankCard(paymentDto: PaymentDto,): BankCardDto {
        val userId = paymentDto.userId!!
        val bankCards = withContext(Dispatchers.IO) {
            bankCardClient.findAll(userId)
        }.collectList().awaitSingle()

        return bankCards.first { card -> card.priority }
    }

    suspend fun executePaymentAndUpdateBookingStatus(priorityBankCard: BankCardDto,
                                                     booking: Booking,
                                                     paymentDto: PaymentDto,
                                                     ) {
        priorityBankCard.balance -= booking.totalPrice
        bankCardClient.run { update(priorityBankCard) }
        paymentDto.status = Payment.PaymentStatus.PAID
        booking.status = Booking.BookingStatus.CONFIRMED
        bookingRepository.save(booking)
    }
}
