package com.codefylabs.Maple.Leaf.rest.dto.user

data class ChangePasswordRequest (
    val oldPassword:String,
    val newPassword:String
)