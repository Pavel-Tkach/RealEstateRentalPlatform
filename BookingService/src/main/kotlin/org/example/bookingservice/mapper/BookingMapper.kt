package org.example.bookingservice.mapper

import org.example.bookingservice.document.Booking
import org.example.bookingservice.dto.BookingDto
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface BookingMapper {

    fun toDto(booking: Booking): BookingDto

    fun toDocument(bookingDto: BookingDto): Booking
}