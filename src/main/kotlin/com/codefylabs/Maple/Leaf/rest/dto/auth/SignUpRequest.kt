package com.codefylabs.Maple.Leaf.rest.dto.auth;


data class SignUpRequest (
    var userName:String,
    var name:String,
    var email:String,
    var password:String
)
