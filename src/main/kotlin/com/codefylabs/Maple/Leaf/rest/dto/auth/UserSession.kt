package com.codefylabs.Maple.Leaf.rest.dto.auth;

import com.codefylabs.Maple.Leaf.persistence.entities.AuthProvider



data class UserSession (
    var userId:Int,
    var name:String,
    var email:String,
    var profilePicture:String?,
    var token:String,
    var refreshToken:String,
    var authProvider: AuthProvider?
)
