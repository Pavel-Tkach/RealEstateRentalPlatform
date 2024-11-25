package org.example.propertyservice.dto

import jakarta.validation.constraints.NotBlank

data class LocationDto(
    @field:NotBlank(message = "Address cannot be empty")
    val address: String?,
    @field:NotBlank(message = "City cannot be empty")
    val city: String?,
    @field:NotBlank(message = "State cannot be empty")
    val state: String?,
    @field:NotBlank(message = "Country cannot be empty")
    val country: String?,
)
