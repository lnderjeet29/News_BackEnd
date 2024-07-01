package com.codefylabs.Maple.Leaf.business.gateway

import com.codefylabs.Maple.Leaf.persistence.entities.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.multipart.MultipartFile


interface UserServices {
    fun userDetailsService(): UserDetailsService?
    fun findUser(username: String?): User?

    fun uploadProfileImage(email:String?,profileImage:MultipartFile):String
}
