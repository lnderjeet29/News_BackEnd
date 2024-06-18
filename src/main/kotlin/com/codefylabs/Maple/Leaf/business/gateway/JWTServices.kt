package com.codefylabs.Maple.Leaf.business.gateway

import org.springframework.security.core.userdetails.UserDetails


interface JWTServices {
    fun extractUserName(token: String?): String?

    fun generateToken(userDetails: UserDetails?): String?

    fun isTokenValid(token: String?, userDetails: UserDetails?): Boolean

    fun generateRefreshToken(extraClaims: Map<String?, Any?>?, userDetails: UserDetails?): String?
}
