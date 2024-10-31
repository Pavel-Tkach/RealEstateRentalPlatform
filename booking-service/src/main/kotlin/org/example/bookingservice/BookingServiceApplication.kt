package org.example.bookingservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import reactivefeign.spring.config.EnableReactiveFeignClients

@SpringBootApplication
@EnableReactiveFeignClients
@EnableDiscoveryClient
class BookingServiceApplication

fun main(args: Array<String>) {
    runApplication<BookingServiceApplication>(*args)
}
