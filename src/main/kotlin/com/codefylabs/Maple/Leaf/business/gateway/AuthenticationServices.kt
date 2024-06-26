package com.codefylabs.Maple.Leaf.business.gateway



import com.codefylabs.Maple.Leaf.rest.dto.SignUpRequest
import com.codefylabs.Maple.Leaf.rest.dto.SigninRequest
import com.codefylabs.Maple.Leaf.persistance.User
import com.codefylabs.Maple.Leaf.rest.dto.GoogleAuthResponseDto
import com.codefylabs.Maple.Leaf.rest.dto.JwtAuthicationResponse
import org.springframework.transaction.annotation.Transactional


interface AuthenticationServices {
    @Transactional
    fun signup(signUpRequest: SignUpRequest?): User?
    fun isExists(email: String?): Boolean
    fun verifyUser(token: String): Boolean
    fun signin(signinRequest: SigninRequest?): JwtAuthicationResponse?
    fun refreshToken(refreshToken: String?): JwtAuthicationResponse?

    fun verifyIdToken(idToken: String): GoogleAuthResponseDto?
}
