package com.codefylabs.Maple.Leaf.business.gateway

import com.codefylabs.Maple.Leaf.persistance.User
import org.springframework.security.core.userdetails.UserDetailsService


interface UserServices {
    fun userDetailsService(): UserDetailsService?
    fun findUser(username: String?): User?
}
