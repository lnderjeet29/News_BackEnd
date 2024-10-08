package com.codefylabs.Maple.Leaf.persistance.services

import com.Inderjeet.News.business.gateway.AuthenticationServices
import com.Inderjeet.News.business.gateway.JWTServices
import com.Inderjeet.News.persistence.entities.AuthProvider
import com.Inderjeet.News.persistence.entities.Role
import com.Inderjeet.News.persistence.entities.User
import com.Inderjeet.News.persistence.repository.UserRepositoryJpa
import com.Inderjeet.News.rest.ExceptionHandler.BadApiRequest
import com.Inderjeet.News.rest.dto.auth.GoogleAuthResponseDto
import com.Inderjeet.News.rest.dto.auth.SignUpRequest
import com.Inderjeet.News.rest.dto.auth.SigninRequest
import com.Inderjeet.News.rest.dto.auth.UserSession
import com.Inderjeet.News.rest.dto.others.MailBody
import com.google.gson.Gson
import com.google.gson.JsonParser
import lombok.RequiredArgsConstructor
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*


@Service
@RequiredArgsConstructor
class AuthenticationServicesImpl(
    val passwordEncoder: PasswordEncoder, val userRepository: UserRepositoryJpa,
    val jwtServices: JWTServices, val authenticationManager: AuthenticationManager,
    val emailServices: com.Inderjeet.News.business.gateway.EmailServices,
) : AuthenticationServices {


    var logger: Logger = LoggerFactory.getLogger(AuthenticationServicesImpl::class.java)
    override fun verifyGoogleIdToken(idToken: String): GoogleAuthResponseDto? {
        val client = OkHttpClient()
        val url = "https://oauth2.googleapis.com/tokeninfo?id_token=$idToken"

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                println("Request failed: ${response.code}")
                return null
            }

            val responseBody = response.body?.string()
            if (responseBody != null) {
                val jsonElement = JsonParser.parseString(responseBody)
                val jsonObject = jsonElement.asJsonObject


                return Gson().fromJson(jsonObject, GoogleAuthResponseDto::class.java)
            }

            println("Response body is null")
            return null
        }
    }

    override fun signInWithGoogle(googleUser: GoogleAuthResponseDto): UserSession {
        if (isExists(googleUser.email)) {
            val user = userRepository.findByEmail(googleUser.email).get()
            if (user.isBlocked) {
                throw BadApiRequest("Your account has been blocked. Please contact support for further assistance.!")
            }
            when (user.authProvider) {
                AuthProvider.GOOGLE -> {
                    val userSession: UserSession


                    val jwt: String = jwtServices.generateToken(user)
                    val refreshToken: String = jwtServices.generateRefreshToken(HashMap(), user)
                    userSession = UserSession(
                        token = jwt,
                        email = user.email.toString(),
                        refreshToken = refreshToken,
                        name = user.name.toString(),
                        userId = user.id.toString().toInt(),
                        authProvider = user.authProvider,
                        profilePicture = user.profilePicture,
                        username = user.userName

                    )

                    return userSession
                }

                AuthProvider.EMAIL_PASSWORD -> {
                    throw BadApiRequest("Account is registered with email and password")
                }

                null -> {
                    throw BadApiRequest("Something went wrong!")
                }
            }
        } else {

            val user =
                User(
                    email = googleUser.email,
                    userName = googleUser.email.substringBefore("@"),
                    name = googleUser.name.lowercase(),
                    role = Role.USER,
                    enabled = true,
                    verificationToken = null,
                    password = null,
                    authProvider = AuthProvider.GOOGLE,
                    profilePicture = googleUser.picture,
                    date = LocalDate.now()
                )

            try {
                userRepository.save(user)
                val userSession: UserSession


                val jwt: String = jwtServices.generateToken(user)
                val refreshToken: String = jwtServices.generateRefreshToken(HashMap(), user)
                userSession = UserSession(
                    token = jwt,
                    email = user.email.toString(),
                    refreshToken = refreshToken,
                    name = user.name.toString(),
                    userId = user.id.toString().toInt(),
                    authProvider = user.authProvider,
                    profilePicture = user.profilePicture,
                    username = user.userName
                )

                return userSession
            } catch (e: Exception) {
                logger.info(user.name + " " + user.email)
                logger.info("exception in saving data")
                logger.info(e.message)
                throw BadApiRequest(e.message ?: "Issue while creating your account.!")
            }

        }
    }


    override fun signup(signUpRequest: SignUpRequest): User {
        if (isExists(signUpRequest.email)) {
            val user = userRepository.findByEmail(email = signUpRequest.email).get()
            if (user.enabled) {
                throw BadApiRequest("User Already Exist!")
            } else {
                userRepository.delete(user)
            }
        }
        if (!isUserNameAvailable(signUpRequest.userName)) {
            throw BadApiRequest("UserName Not Available!")
        }
        val user =
            User(
                email = signUpRequest.email,
                name = signUpRequest.name.lowercase(),
                userName = signUpRequest.userName,
                role = Role.USER,
                enabled = false,
                verificationToken = UUID.randomUUID().toString(),
                password = passwordEncoder.encode(signUpRequest.password),
                authProvider = AuthProvider.EMAIL_PASSWORD,
                date = LocalDate.now()
            )

        val data: User = userRepository.save(user)

        sendVerificationEmail(user)
        return data
    }

    override fun isUserNameAvailable(userName: String?): Boolean {
        if (userName.isNullOrEmpty()) {
            return false
        }
        return !userRepository.existsByUserName(userName.lowercase())
    }

    override fun updateUserName(userName: String?, email: String) {
        try {
            val user = userRepository.findByEmail(email).get()
            user.userName = userName
            userRepository.save(user)
        } catch (e: Exception) {
            throw BadApiRequest("Something went wrong!")
        }
    }

    override fun isExists(email: String?): Boolean {
        val user: Optional<User> = userRepository.findByEmail(email)
        return user.isPresent
    }


    fun sendVerificationEmail(user: User) {
        val message = MailBody(
            user.email,
            "Email Verification",
            "Click the link to verify your email: https://mapleleaf.codefy-testing.com/api/v1/auth/verify?token=${user.verificationToken}"
        )
        emailServices.sendSimpleMessage(message)
    }


    override fun verifyUser(token: String): Boolean {
        val user = userRepository.findByVerificationToken(token) ?: return false
        user.get().enabled = true
        user.get().verificationToken = null
        userRepository.save(user.get())
        return true
    }


    override fun signin(signinRequest: SigninRequest?): UserSession? {


        val user = userRepository.findByEmail(signinRequest?.email)
            .orElseThrow { BadApiRequest("This email is not registered. Please sign up.") }

        //check user verified or not
        if (!user.enabled) {
            throw BadApiRequest("Your email address is not verified.!")
        }
        if (user.isBlocked) {
            throw BadApiRequest("Your account has been blocked. Please contact support for further assistance.!")
        }
        if (user.authProvider == AuthProvider.GOOGLE) {
            throw BadApiRequest("This email is connected to Google Sign-In. Please continue with Google.")
        }
        var userSession: UserSession? = null

        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                signinRequest?.email,
                signinRequest?.password
            )
        )

        val jwt: String = jwtServices.generateToken(user)
        val refreshToken: String = jwtServices.generateRefreshToken(HashMap(), user)
        userSession = UserSession(
            token = jwt,
            email = user.email.toString(),
            refreshToken = refreshToken,
            name = user.name.toString(),
            userId = user.id.toString().toInt(),
            authProvider = user.authProvider,
            profilePicture = user.profilePicture,
            username = user.userName
        )

        return userSession
    }


    override fun refreshToken(refreshToken: String?): UserSession? {
        val userEmail: String? = jwtServices?.extractEmail(refreshToken)
        val user: User? = userRepository?.findByEmail(userEmail)?.orElseThrow { BadApiRequest("User not found!") }
        if (jwtServices?.isTokenValid(refreshToken, user) == true) {
            val jwt: String? = jwtServices.generateToken(user)
            val userSession = userEmail?.let {
                UserSession(
                    token = jwt.toString(),
                    email = it,
                    refreshToken = refreshToken.toString(),
                    name = user?.name.toString(),
                    userId = user?.id.toString().toInt(),
                    authProvider = user?.authProvider,
                    profilePicture = null,
                    username = user?.userName
                )
            }
            return userSession
        }
        return null
    }

    override fun changePassword(newPassword: String?, oldPassword: String?, email: String?): String {
        val user = userRepository.findByEmail(email).get()
        if (user.isBlocked) {
            throw BadApiRequest("Your account has been blocked. Please contact support for further assistance.!")
        }
        if (user.authProvider === AuthProvider.GOOGLE) {
            throw BadApiRequest("Cannot change password for Google Sign-In email.")
        }

        if (!passwordEncoder.matches(oldPassword, user.password)) {
            throw BadCredentialsException("Incorrect old password.")
        }
        try {
            val password = passwordEncoder.encode(newPassword)
            userRepository.updatePassword(email, password)
        } catch (e: Exception) {
            throw BadApiRequest("Error occurred during password change.!")
        }
        return "Password changed successfully."
    }

}


