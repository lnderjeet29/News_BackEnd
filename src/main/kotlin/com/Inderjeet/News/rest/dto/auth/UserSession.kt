package com.Inderjeet.News.rest.dto.auth;

import com.Inderjeet.News.persistence.entities.AuthProvider



data class UserSession (
    var userId:Int,
    var username:String?,
    var name:String,
    var email:String,
    var profilePicture:String?,
    var token:String,
    var refreshToken:String,
    var authProvider: AuthProvider?
)

