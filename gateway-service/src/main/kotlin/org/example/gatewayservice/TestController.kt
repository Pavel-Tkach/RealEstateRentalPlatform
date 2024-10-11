package org.example.gatewayservice

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

    @GetMapping
    fun hello(): String { return "hello world" }

    @GetMapping("/secured")
    fun secured(): String { return "secured hello world" }
}