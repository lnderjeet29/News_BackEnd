package com.codefylabs.Maple.Leaf.persistance.Implementation

import com.codefylabs.Maple.Leaf.business.gateway.AuthenticationServices
import com.codefylabs.Maple.Leaf.business.gateway.EmailServices
import com.codefylabs.Maple.Leaf.business.gateway.JWTServices
import com.codefylabs.Maple.Leaf.persistance.Role
import com.codefylabs.Maple.Leaf.persistance.User
import com.codefylabs.Maple.Leaf.persistance.UserRepositoryJpa
import com.codefylabs.Maple.Leaf.persistence.AuthProviders
import com.codefylabs.Maple.Leaf.rest.ExceptionHandler.BadApiRequest
import com.codefylabs.Maple.Leaf.rest.dto.*
import com.google.gson.Gson
import com.google.gson.JsonParser
import lombok.RequiredArgsConstructor
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*
import kotlin.random.Random


@Service
@RequiredArgsConstructor
class AuthenticationServicesImpl(
    val passwordEncoder: PasswordEncoder, val userRepository: UserRepositoryJpa,
    val jwtServices: JWTServices, val authenticationManager: AuthenticationManager,
    val emailServices: EmailServices,
) : AuthenticationServices {


    var logger = LoggerFactory.getLogger(AuthenticationServicesImpl::class.java)
    override fun verifyIdToken(idToken: String): GoogleAuthResponseDto? {
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


    override fun signup(signUpRequest: SignUpRequest?): User? {

        val user =
            User(
                id = Random.nextInt(100_00, 999_99),
                email = signUpRequest?.email,
                name = signUpRequest?.name,
                role = Role.USER,
                enabled = false,
                verificationToken = UUID.randomUUID().toString(),
                password = passwordEncoder!!.encode(signUpRequest?.password),
                authProvider = AuthProviders.EMAIL_PASSWORD
            )

        val data: User? = try {
            userRepository.save(user)
        } catch (e: Exception) {
            logger.info(user.name + " " + user.email)
            logger.info("exception in saving data")
            logger.info(e.message)
            return null
        }
        sendVerificationEmail(user)
        return data
    }


    override fun isExists(email: String?): Boolean {
        val user: Optional<User> = userRepository.findByEmail(email)
        if (!user.isPresent) return false

        return true
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


    override fun signin(signinRequest: SigninRequest?): JwtAuthicationResponse? {


        val user: User? = userRepository.findByEmail(signinRequest?.email)
            .orElseThrow { BadApiRequest("This email is not registered. Please sign up.") }

        //check user verified or not
        if (user?.enabled == false) {
            throw BadApiRequest("Your email address is not verified.!")
        }
        if (user?.isBlocked == true) {
            throw BadApiRequest("Your account has been blocked. Please contact support for further assistance.!")
        }

        var jwtAuthicationResponse: JwtAuthicationResponse? = null

        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                signinRequest?.email,
                signinRequest?.password
            )
        )

        val jwt: String? = jwtServices?.generateToken(user)
        val refreshToken: String? = jwtServices?.generateRefreshToken(HashMap(), user)
        jwtAuthicationResponse = jwt?.let {
            refreshToken?.let { it1 ->
                JwtAuthicationResponse(
                    token = it,
                    email = user?.email.toString(),
                    refreshToken = it1,
                    name = user?.name.toString(),
                    userId = user?.id.toString().toInt(),
                    authProvider = user?.authProvider,
                    profilePicture = "null"
                )
            }
        }

        return jwtAuthicationResponse
    }


    override fun refreshToken(refreshToken: String?): JwtAuthicationResponse? {
        val userEmail: String? = jwtServices?.extractUserName(refreshToken)
        val user: User? = userRepository?.findByEmail(userEmail)?.orElseThrow{BadApiRequest("User not found!")}
        if (jwtServices?.isTokenValid(refreshToken, user) == true) {
            val jwt: String? = jwtServices.generateToken(user)
            val jwtAuthicationResponse = userEmail?.let {
                JwtAuthicationResponse(
                    token = jwt.toString(),
                    email = it,
                    refreshToken = refreshToken.toString(),
                    name = user?.name.toString(),
                    userId = user?.id.toString().toInt(),
                    authProvider = user?.authProvider,
                    profilePicture = "null"
                )
            }
            return jwtAuthicationResponse
        }
        return null
    }

}


