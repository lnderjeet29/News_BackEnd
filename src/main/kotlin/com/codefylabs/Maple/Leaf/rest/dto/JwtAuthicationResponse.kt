package com.codefylabs.Maple.Leaf.rest.dto;

import lombok.Builder


@Builder
data class JwtAuthicationResponse (
    var Token:String,
    var refreshToken:String,
    var email:String,
    var status:Boolean
)
