package org.example.propertyservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.context.annotation.EnableAspectJAutoProxy
import reactivefeign.spring.config.EnableReactiveFeignClients

@SpringBootApplication
@EnableReactiveFeignClients
@EnableDiscoveryClient
@EnableAspectJAutoProxy
class PropertyServiceApplication

fun main(args: Array<String>) {
    runApplication<PropertyServiceApplication>(*args)
}
