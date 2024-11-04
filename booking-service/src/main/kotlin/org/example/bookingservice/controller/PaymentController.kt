package org.example.bookingservice.controller

import org.example.bookingservice.aspect.Loggable
import org.example.bookingservice.dto.PaymentDto
import org.example.bookingservice.service.PaymentService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class PaymentController(
    private val paymentService: PaymentService,
) {

    @Loggable
    @GetMapping("/payments")
    suspend fun findAllPayments(@RequestHeader("x-auth-user-id") userId: String,): List<PaymentDto> = paymentService.findAllByUserId(userId)

    @Loggable
    @GetMapping("/payments/{paymentId}")
    suspend fun findPaymentById(@PathVariable paymentId: String,
                                @RequestHeader("x-auth-user-id") userId: String,
                                ): PaymentDto  = paymentService.findById(userId)
}
