package com.Inderjeet.News.rest.dto

import com.Inderjeet.News.persistence.entities.AuthProvider
import com.Inderjeet.News.persistence.entities.Role
import java.time.LocalDate


data class UserDto(
    var id: Int?=0,
    var name: String? = null,
    var email: String? = null,
    var enabled: Boolean = false,
    var isBlocked: Boolean = false,
    var date: LocalDate?= LocalDate.now(),
    var role: Role? = null,
    var authProvider: AuthProvider?=null,
    var profilePicture: String?=null,
    var userName:String?=null
)
