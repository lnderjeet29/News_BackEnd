package com.codefylabs.Maple.Leaf.rest.dto;

import com.codefylabs.Maple.Leaf.rest.dto.JwtAuthicationResponse


data class ApiRepositoryMesssage(
    var message:String,
    var status:Boolean,
    var response: JwtAuthicationResponse?
)
