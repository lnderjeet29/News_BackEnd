package com.codefylabs.Maple.Leaf.rest.Controller

import com.codefylabs.Maple.Leaf.business.gateway.AuthenticationServices
import com.codefylabs.Maple.Leaf.persistance.User
import com.codefylabs.Maple.Leaf.persistance.UserRepositoryJpa
import com.codefylabs.Maple.Leaf.rest.dto.*
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(val authentictionSerives: AuthenticationServices) {

    var logger = LoggerFactory.getLogger(AuthController::class.java)


    @PostMapping("/signup")
    fun signup(@RequestBody signUpRequest: SignUpRequest?): ResponseEntity<ApiUserMessage> {
        try {
            if (authentictionSerives.isExists(signUpRequest?.email)) {
                val message: ApiUserMessage =
                    ApiUserMessage(message = "user already exists...", status = false, response = null)
                return ResponseEntity<ApiUserMessage>(message, HttpStatus.CONFLICT)
            }
            val user: User? = authentictionSerives?.signup(signUpRequest)
            val message = ApiUserMessage(response = user, message = ("user sign up successfully..."), status = true)
            return ResponseEntity<ApiUserMessage>(message, HttpStatus.ACCEPTED)
        } catch (e: Exception) {
            logger.info(e.message)
            val message: ApiUserMessage =
                ApiUserMessage(message = "credential false...", status = false, response = null)
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
            val messsage: ApiRepositoryMesssage
            if (response?.status == true) {
                messsage =
                    ApiRepositoryMesssage(response = response, message = "Login successfully...", status = true)
                ResponseEntity<ApiRepositoryMesssage>(messsage, HttpStatus.ACCEPTED)
            } else {
                messsage =
                    ApiRepositoryMesssage(response = response, message = "Oops something went wrong", status = false)
                ResponseEntity<ApiRepositoryMesssage>(messsage, HttpStatus.BAD_REQUEST)
            }
        } catch (e: Exception) {
            val message: ApiRepositoryMesssage =
                ApiRepositoryMesssage(response = null, message = "something went wrong...", status = false)
            ResponseEntity<ApiRepositoryMesssage>(message, HttpStatus.BAD_REQUEST)
        }
    }


    @GetMapping("/refresh")
    fun refresh(@RequestHeader("Authorization") request: String): ResponseEntity<ApiRepositoryMesssage?> {
        return try {
            val response: JwtAuthicationResponse? = authentictionSerives?.refreshToken(request.substring(7))
            ResponseEntity.ok(
                ApiRepositoryMesssage(response = response, message = "successful", status = true)
            )
        } catch (e: Exception) {
            ResponseEntity.badRequest()
                .body(ApiRepositoryMesssage(response = null, message = "something went wrong...", status = false))
        }
    }

}

