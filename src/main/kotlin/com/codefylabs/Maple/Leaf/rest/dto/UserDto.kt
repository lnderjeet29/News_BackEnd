package com.codefylabs.Maple.Leaf.rest.dto

import com.codefylabs.Maple.Leaf.persistance.Role
import com.codefylabs.Maple.Leaf.persistence.AuthProvider
import java.time.LocalDate


data class UserDto(
    var id: Int?=0,
    var name: String? = null,
    private var password: String? = null,
    var email: String? = null,
    var enabled: Boolean = false,
    var isBlocked: Boolean = false,
    var date: LocalDate?= LocalDate.now(),
    var role: Role? = null,
    var authProvider: AuthProvider?=null,
    var profilePicture: String?=null
)
