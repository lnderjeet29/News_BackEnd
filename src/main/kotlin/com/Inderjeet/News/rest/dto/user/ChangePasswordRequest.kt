package com.Inderjeet.News.rest.dto.user

data class ChangePasswordRequest (
    val oldPassword:String,
    val newPassword:String
)