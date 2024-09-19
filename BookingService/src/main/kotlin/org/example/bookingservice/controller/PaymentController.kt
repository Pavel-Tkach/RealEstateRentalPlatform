package org.example.bookingservice.controller

import org.example.bookingservice.dto.PaymentDto
import org.example.bookingservice.service.api.PaymentService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/payments")
class PaymentController(
    private val paymentService: PaymentService,
) {

    @GetMapping
    suspend fun findAllPayments(@RequestParam tenantId: String): List<PaymentDto> {
        return paymentService.findAllByTenantId(tenantId)
    }

    @GetMapping("/{paymentId}")
    suspend fun findPaymentById(@PathVariable paymentId: String,
                                @RequestParam tenantId: String,): PaymentDto {
        return paymentService.findById(tenantId)
    }
}