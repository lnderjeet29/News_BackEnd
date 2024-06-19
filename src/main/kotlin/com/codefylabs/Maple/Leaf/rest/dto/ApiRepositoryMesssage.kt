package com.codefylabs.Maple.Leaf.rest.dto;



data class ApiRepositoryMesssage(
    var message:String,
    var status:Boolean,
    var response: JwtAuthicationResponse?
)
