package com.codefylabs.Maple.Leaf.rest.dto.auth;

import org.springframework.web.multipart.MultipartFile

data class SignUpRequest (
    var name:String,
    var email:String,
    var password:String
)
