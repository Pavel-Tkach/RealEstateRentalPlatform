package org.example.bookingservice.mapper

import org.example.bookingservice.document.Payment
import org.example.bookingservice.dto.PaymentDto
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface PaymentMapper {

    fun toDto(payment: Payment) : PaymentDto

    fun toDocument(paymentDto: PaymentDto): Payment
}