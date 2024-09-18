package org.example.propertyservice.repository

import org.example.propertyservice.document.Property
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PropertyRepository: CoroutineCrudRepository<Property, String> {

}