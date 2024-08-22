package com.Inderjeet.News.business.gateway

import com.Inderjeet.News.persistence.entities.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.multipart.MultipartFile


interface UserServices {
    fun userDetailsService(): UserDetailsService?
    fun findUser(username: String?): User?

    fun uploadProfileImage(email:String?,profileImage:MultipartFile):String

    fun updateName(email: String,name:String):Boolean
}
