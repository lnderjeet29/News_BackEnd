package com.codefylabs.Maple.Leaf.rest.dto

import com.codefylabs.Maple.Leaf.persistence.entities.Role
import com.codefylabs.Maple.Leaf.persistence.entities.AuthProvider
import com.codefylabs.Maple.Leaf.persistence.entities.User
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

//fun User.toDto()=UserDto(
//    id= this.id,
//    name= this.name,
//    email = this.email,
//    enabled= this.isEnabled,
//    isBlocked = this.isBlocked,
//    date= this.date,
//    role = this.role,
//    authProvider=this.authProvider,
//    profilePicture=this.profilePicture
//)
