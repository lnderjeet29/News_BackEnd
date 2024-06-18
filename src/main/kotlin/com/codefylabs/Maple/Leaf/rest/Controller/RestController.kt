package com.codefylabs.Maple.Leaf.rest.Controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RestController {
    @GetMapping("/say")
    fun say():String{
        return "hello, welcome to codefylabs..."
    }
}