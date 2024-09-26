package org.example.propertyservice.exception

class PropertyNotFoundException(
    override val message: String? = null
) : RuntimeException()
