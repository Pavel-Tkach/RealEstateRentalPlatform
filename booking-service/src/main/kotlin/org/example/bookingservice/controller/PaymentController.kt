package org.example.bookingservice.controller

import org.example.bookingservice.dto.PaymentDto
import org.example.bookingservice.service.PaymentService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class PaymentController(
    private val paymentService: PaymentService,
) {

    @GetMapping("/payments")
    suspend fun findAllPayments(@RequestParam userId: String): List<PaymentDto> = paymentService.findAllByUserId(userId)

    @GetMapping("/payments/{paymentId}")
    suspend fun findPaymentById(@PathVariable paymentId: String,
                                @RequestParam userId: String,
                                ): PaymentDto  = paymentService.findById(userId)
}
