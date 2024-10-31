package org.example.propertyservice.exception

import org.example.propertyservice.dto.ErrorDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.lang.Exception

@RestControllerAdvice
class RestApiExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = [PropertyNotFoundException::class])
    fun handleNotFoundException(exception: Exception): ErrorDto {
        val name = "Error"
        val message = exception.localizedMessage

        return ErrorDto(name, message)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = [IllegalRightsException::class])
    fun handleIllegalRightsException(exception: IllegalRightsException): ErrorDto {
        val name = "Not enough rights"
        val message = exception.localizedMessage

        return ErrorDto(name, message)
    }
}