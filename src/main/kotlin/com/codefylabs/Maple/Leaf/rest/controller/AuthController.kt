package com.codefylabs.Maple.Leaf.rest.controller

import com.codefylabs.Maple.Leaf.business.gateway.AuthenticationServices
import com.codefylabs.Maple.Leaf.persistance.User
import com.codefylabs.Maple.Leaf.rest.ExceptionHandler.BadApiRequest
import com.codefylabs.Maple.Leaf.rest.dto.*
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(val authentictionSerives: AuthenticationServices) {

    var logger = LoggerFactory.getLogger(AuthController::class.java)


    @PostMapping("/signup")
    fun signup(@RequestBody signUpRequest: SignUpRequest?): ResponseEntity<ApiUserMessage> {
        try {
            if (authentictionSerives.isExists(signUpRequest?.email)) {
                val message: ApiUserMessage =
                    ApiUserMessage(message = "user already exists...", status = false, data = null)
                return ResponseEntity<ApiUserMessage>(message, HttpStatus.CONFLICT)
            }
            val user: User? = authentictionSerives?.signup(signUpRequest)
            val message = ApiUserMessage(data = null, message = ("A verification email has been sent to your email address."), status = true)
            return ResponseEntity<ApiUserMessage>(message, HttpStatus.ACCEPTED)
        } catch (e: Exception) {
            logger.info(e.message)
            val message: ApiUserMessage =
                ApiUserMessage(message = "credential false...", status = false, data = null)
            return ResponseEntity<ApiUserMessage>(message, HttpStatus.BAD_REQUEST)
        }
    }


    @GetMapping("/verify")
    fun verifyUser(@RequestParam token: String): ResponseEntity<String> {
        return if (authentictionSerives.verifyUser(token)) {
            ResponseEntity.ok("Email verified successfully!")
        } else {
            ResponseEntity.badRequest().body("Invalid verification token.")
        }
    }


    @PostMapping("/signin")
    fun signin(@RequestBody signinRequest: SigninRequest?): ResponseEntity<ApiRepositoryMesssage> {
        return try {
            val response: JwtAuthicationResponse? = authentictionSerives?.signin(signinRequest)
            val message: ApiRepositoryMesssage=
                    ApiRepositoryMesssage(data = response, message = "Login successfully...", status = true)
                ResponseEntity<ApiRepositoryMesssage>(message, HttpStatus.ACCEPTED)

        } catch (e: Exception) {
            when(e)
            {
                is BadApiRequest->{
                    val message: ApiRepositoryMesssage =
                        ApiRepositoryMesssage(data = null, message = e.message.toString(), status = false)
                    ResponseEntity<ApiRepositoryMesssage>(message, HttpStatus.BAD_REQUEST)

                }
                is BadCredentialsException->{
                    val message: ApiRepositoryMesssage =
                        ApiRepositoryMesssage(data = null, message = "Invalid Password!", status = false)
                    ResponseEntity<ApiRepositoryMesssage>(message, HttpStatus.BAD_REQUEST)
                }
                else->
                {
                    val message: ApiRepositoryMesssage =
                        ApiRepositoryMesssage(data = null, message = e.message  ?: "Something went wrong!", status = false)
                    ResponseEntity<ApiRepositoryMesssage>(message, HttpStatus.BAD_REQUEST)
                }
            }

        }
    }


    @PostMapping("/refresh")
    fun refresh(@RequestHeader("Authorization") request: String): ResponseEntity<ApiRepositoryMesssage?> {
        return try {
            val response: JwtAuthicationResponse? = authentictionSerives?.refreshToken(request.substring(7))
            ResponseEntity.ok(
                ApiRepositoryMesssage(data = response, message = "successful", status = true)
            )
        } catch (e: Exception) {
            ResponseEntity.badRequest()
                .body(ApiRepositoryMesssage(data = null, message = "something went wrong...", status = false))
        }
    }




    @GetMapping("/signin-with-google")
    fun verifyToken(@RequestParam(value = "token") idToken: String): Any {
        val userInfo = authentictionSerives.verifyIdToken(idToken)
        return userInfo ?: "Invalid ID token"
    }

}
