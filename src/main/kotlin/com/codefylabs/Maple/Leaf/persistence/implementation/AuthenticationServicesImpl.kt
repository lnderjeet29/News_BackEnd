package com.codefylabs.Maple.Leaf.persistance.Implementation

import com.codefylabs.Maple.Leaf.business.gateway.AuthenticationServices
import com.codefylabs.Maple.Leaf.business.gateway.EmailServices
import com.codefylabs.Maple.Leaf.business.gateway.JWTServices
import com.codefylabs.Maple.Leaf.persistance.Role
import com.codefylabs.Maple.Leaf.persistance.User
import com.codefylabs.Maple.Leaf.persistance.UserRepositoryJpa
import com.codefylabs.Maple.Leaf.persistence.AuthProvider
import com.codefylabs.Maple.Leaf.rest.ExceptionHandler.BadApiRequest
import com.codefylabs.Maple.Leaf.rest.dto.auth.GoogleAuthResponseDto
import com.codefylabs.Maple.Leaf.rest.dto.auth.SignUpRequest
import com.codefylabs.Maple.Leaf.rest.dto.auth.SigninRequest
import com.codefylabs.Maple.Leaf.rest.dto.auth.UserSession
import com.codefylabs.Maple.Leaf.rest.dto.others.MailBody
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

    override fun signInWithGoogle(googleUser:GoogleAuthResponseDto): UserSession {
       if(isExists(googleUser.email)){
           val user=userRepository.findByEmail(googleUser.email).get()
           if (user.isBlocked) {
               throw BadApiRequest("Your account has been blocked. Please contact support for further assistance.!")
           }
           when (user.authProvider){
               AuthProvider.GOOGLE -> {
                   val userSession: UserSession


                   val jwt: String = jwtServices.generateToken(user)
                   val refreshToken: String = jwtServices.generateRefreshToken(HashMap(), user)
                   userSession =  UserSession(
                       token = jwt,
                       email = user.email.toString(),
                       refreshToken = refreshToken,
                       name = user.name.toString(),
                       userId = user.id.toString().toInt(),
                       authProvider = user.authProvider,
                       profilePicture = user.profilePicture
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
       }else{
           val user =
               User(
                   email = googleUser.email,
                   name = googleUser.name,
                   role = Role.USER,
                   enabled = true,
                   verificationToken = null,
                   password = null,
                   authProvider = AuthProvider.GOOGLE,
                   profilePicture = googleUser.picture
               )

           try {
               userRepository.save(user)
               val userSession: UserSession



               val jwt: String = jwtServices.generateToken(user)
               val refreshToken: String = jwtServices.generateRefreshToken(HashMap(), user)
               userSession =  UserSession(
                   token = jwt,
                   email = user.email.toString(),
                   refreshToken = refreshToken,
                   name = user.name.toString(),
                   userId = user.id.toString().toInt(),
                   authProvider = user.authProvider,
                   profilePicture = user.profilePicture
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


    override fun signup(signUpRequest: SignUpRequest?): User? {

        val user =
            User(
                email = signUpRequest?.email,
                name = signUpRequest?.name,
                role = Role.USER,
                enabled = false,
                verificationToken = UUID.randomUUID().toString(),
                password = passwordEncoder.encode(signUpRequest?.password),
                authProvider = AuthProvider.EMAIL_PASSWORD
            )

        val data: User? = try {
            userRepository.save(user)
        } catch (e: Exception) {
            e.printStackTrace()
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
        if(user.authProvider==AuthProvider.GOOGLE){
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
            profilePicture = user.profilePicture
        )

        return userSession
    }


    override fun refreshToken(refreshToken: String?): UserSession? {
        val userEmail: String? = jwtServices?.extractUserName(refreshToken)
        val user: User? = userRepository?.findByEmail(userEmail)?.orElseThrow{BadApiRequest("User not found!")}
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
                    profilePicture = "null"
                )
            }
            return userSession
        }
        return null
    }



}


