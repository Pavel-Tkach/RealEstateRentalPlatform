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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class BookingController(
    private val bookingService: BookingService,
    private val paymentService: PaymentService,
) {

    @GetMapping("/bookings")
    suspend fun findAllBookings(@RequestParam tenantId: String): List<BookingDto> {
        return bookingService.findAllByTenantId(tenantId)
    }

    @GetMapping("/bookings/{bookingId}")
    suspend fun findBookingById(@PathVariable bookingId: String): BookingDto {
        return bookingService.findById(bookingId)
    }

    @PostMapping("/bookings")
    suspend fun createBooking(@RequestBody bookingDto: BookingDto): BookingDto {
        return bookingService.create(bookingDto)
    }

    @PostMapping("/bookings/{bookingId}/payments")
    suspend fun makePayment(@PathVariable bookingId: String,
                            @RequestBody paymentDto: PaymentDto,): PaymentDto {
        return paymentService.create(bookingId, paymentDto)
    }

    @DeleteMapping("/bookings/{bookingId}")
    suspend fun deleteBookingById(@PathVariable bookingId: String) {
        bookingService.deleteById(bookingId)
    }
}
