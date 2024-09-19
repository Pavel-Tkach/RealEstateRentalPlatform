package org.example.bookingservice.controller

import org.example.bookingservice.dto.BookingDto
import org.example.bookingservice.dto.PaymentDto
import org.example.bookingservice.service.api.BookingService
import org.example.bookingservice.service.api.PaymentService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/bookings")
class BookingController(
    private val bookingService: BookingService,
    private val paymentService: PaymentService,
) {

    @GetMapping
    suspend fun findAllBookings(@RequestParam tenantId: String): List<BookingDto> {
        return bookingService.findAllByTenantId(tenantId)
    }

    @GetMapping("/{bookingId}")
    suspend fun findBookingById(@PathVariable bookingId: String): BookingDto {
        return bookingService.findById(bookingId)
    }

    @PostMapping
    suspend fun createBooking(@RequestBody bookingDto: BookingDto): BookingDto {
        return bookingService.create(bookingDto)
    }

    @PostMapping("/{bookingId}/payments")
    suspend fun makePayment(@PathVariable bookingId: String,
                            @RequestBody paymentDto: PaymentDto,): PaymentDto {
        return paymentService.create(bookingId, paymentDto)
    }

    @DeleteMapping("/{bookingId}")
    suspend fun deleteBookingById(@PathVariable bookingId: String) {
        bookingService.deleteById(bookingId)
    }
}