package com.codefylabs.Maple.Leaf.rest.dto

import com.codefylabs.Maple.Leaf.persistance.Role
import com.codefylabs.Maple.Leaf.persistence.AuthProvider

data class UserDto(
    var id: Int?=null,
    var userName: String? = null,
    private var password: String? = null,
    var email: String? = null,
    var enabled: Boolean = false,
    var verificationToken: String? = null,
    var forRest: Boolean= false,
    var isBlocked: Boolean = false,
    var role: Role? = null,
    var authProvider: AuthProvider?=null
)
