package org.example.bookingservice.controller

import org.example.bookingservice.dto.BookingDto
import org.example.bookingservice.dto.PaymentDto
import org.example.bookingservice.service.BookingService
import org.example.bookingservice.service.PaymentService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class BookingController(
    private val bookingService: BookingService,
    private val paymentService: PaymentService,
) {

    @GetMapping("/bookings")
    suspend fun findAllBookings(@RequestHeader("x-auth-user-id") userId: String,): List<BookingDto> = bookingService.findAllByUserId(userId)

    @GetMapping("/bookings/{bookingId}")
    suspend fun findBookingById(@PathVariable bookingId: String): BookingDto = bookingService.findById(bookingId)

    @PostMapping("/bookings")
    suspend fun createBooking(@RequestBody bookingDto: BookingDto,
                              @RequestHeader("x-auth-user-id") userId: String,
                              ): BookingDto = bookingService.create(bookingDto, userId)

    @PostMapping("/bookings/{bookingId}/payments")
    suspend fun makePayment(@PathVariable bookingId: String,
                            @RequestBody paymentDto: PaymentDto,
                            @RequestHeader("x-auth-user-id") userId: String,
                            ): PaymentDto = paymentService.create(bookingId, paymentDto, userId)

    @DeleteMapping("/bookings/{bookingId}")
    suspend fun deleteBookingById(@PathVariable bookingId: String) = bookingService.deleteById(bookingId)
}
