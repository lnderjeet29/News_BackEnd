package com.Inderjeet.News.business.gateway
import com.Inderjeet.News.rest.dto.auth.SigninRequest
import com.Inderjeet.News.persistence.entities.User
import com.Inderjeet.News.rest.dto.auth.SignUpRequest
import com.Inderjeet.News.rest.dto.auth.GoogleAuthResponseDto
import com.Inderjeet.News.rest.dto.auth.UserSession
import org.springframework.transaction.annotation.Transactional

interface AuthenticationServices {
    @Transactional
    fun signup(signUpRequest: SignUpRequest): User

    fun isUserNameAvailable(userName:String?):Boolean
    fun updateUserName(userName:String?,email: String)
    fun isExists(email: String?): Boolean
    fun verifyUser(token: String): Boolean
    fun signin(signinRequest: SigninRequest?): UserSession?
    fun refreshToken(refreshToken: String?): UserSession?

    fun signInWithGoogle(googleUser:GoogleAuthResponseDto):UserSession?
    fun verifyGoogleIdToken(idToken: String): GoogleAuthResponseDto?

    fun changePassword(newPassword: String?,oldPassword:String?, email:String?): String
}
