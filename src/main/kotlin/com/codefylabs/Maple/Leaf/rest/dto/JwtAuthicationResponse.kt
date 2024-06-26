package com.codefylabs.Maple.Leaf.rest.dto;

import com.codefylabs.Maple.Leaf.persistence.AuthProviders
import lombok.Builder


@Builder
data class JwtAuthicationResponse (
    var userId:Int,
    var name:String,
    var email:String,
    var profilePicture:String,
    var token:String,
    var refreshToken:String,
    var authProvider:AuthProviders?
)
