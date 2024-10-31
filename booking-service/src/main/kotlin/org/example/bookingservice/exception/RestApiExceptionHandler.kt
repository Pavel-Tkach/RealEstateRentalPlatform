package org.example.bookingservice.exception

import org.example.bookingservice.dto.ErrorDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.lang.Exception

@RestControllerAdvice
class RestApiExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = [BookingNotFoundException::class,
                                PaymentNotFoundException::class])
    fun handleNotFoundException(exception: Exception): ErrorDto {
        val name = "Error"
        val message = exception.localizedMessage

        return ErrorDto(name, message)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = [NotEnoughMoneyOnCardException::class])
    fun handleNotEnoughMoneyOnCardException(exception: NotEnoughMoneyOnCardException): ErrorDto {
        val name = "Error during make payment"
        val message = exception.localizedMessage

        return ErrorDto(name, message)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = [PropertyAlreadyBookedException::class])
    fun handlePropertyAlreadyBookedException(exception: PropertyAlreadyBookedException): ErrorDto {
        val name = "Error during booking property"
        val message = exception.localizedMessage

        return ErrorDto(name, message)
    }
}