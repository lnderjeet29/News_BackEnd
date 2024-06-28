package com.codefylabs.Maple.Leaf.business.gateway



import com.codefylabs.Maple.Leaf.rest.dto.auth.SignUpRequest
import com.codefylabs.Maple.Leaf.rest.dto.auth.SigninRequest
import com.codefylabs.Maple.Leaf.persistence.entities.User
import com.codefylabs.Maple.Leaf.rest.dto.auth.GoogleAuthResponseDto
import com.codefylabs.Maple.Leaf.rest.dto.auth.UserSession
import org.springframework.transaction.annotation.Transactional


interface AuthenticationServices {
    @Transactional
    fun signup(signUpRequest: SignUpRequest?): User?
    fun isExists(email: String?): Boolean
    fun verifyUser(token: String): Boolean
    fun signin(signinRequest: SigninRequest?): UserSession?
    fun refreshToken(refreshToken: String?): UserSession?

    fun signInWithGoogle(googleUser:GoogleAuthResponseDto):UserSession?
    fun verifyGoogleIdToken(idToken: String): GoogleAuthResponseDto?

    fun changePassword(newPassword: String?,oldPassword:String?, email:String?): String
}
