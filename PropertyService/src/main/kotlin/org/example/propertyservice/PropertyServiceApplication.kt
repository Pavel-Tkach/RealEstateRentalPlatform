package org.example.propertyservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class PropertyServiceApplication

fun main(args: Array<String>) {
    runApplication<PropertyServiceApplication>(*args)
}
