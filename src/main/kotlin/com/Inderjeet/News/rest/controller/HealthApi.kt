package com.Inderjeet.News.rest.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthApi {
    @GetMapping("/")
    fun health(): ResponseEntity<Map<String,String>> {
        return ResponseEntity.ok(mapOf("status" to "up"))

    }
}