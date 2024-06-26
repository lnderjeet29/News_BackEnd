package com.codefylabs.Maple.Leaf.rest.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RestController {
    @GetMapping("/test")
    fun say():String{
        return "hello, welcome to codefylabs..."
    }
}