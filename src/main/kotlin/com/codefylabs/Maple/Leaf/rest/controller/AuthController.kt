package com.codefylabs.Maple.Leaf.rest.controller

import com.codefylabs.Maple.Leaf.business.gateway.AuthenticationServices
import com.codefylabs.Maple.Leaf.business.gateway.JWTServices
import com.codefylabs.Maple.Leaf.persistence.entities.User
import com.codefylabs.Maple.Leaf.rest.ExceptionHandler.BadApiRequest
import com.codefylabs.Maple.Leaf.rest.dto.*
import com.codefylabs.Maple.Leaf.rest.dto.auth.SignInWithGoogleRequest
import com.codefylabs.Maple.Leaf.rest.dto.auth.SignUpRequest
import com.codefylabs.Maple.Leaf.rest.dto.auth.SigninRequest
import com.codefylabs.Maple.Leaf.rest.dto.auth.UserSession
import com.codefylabs.Maple.Leaf.rest.dto.user.ChangePasswordRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(val authentictionSerives: AuthenticationServices, val jwtServices:JWTServices) {

    var logger = LoggerFactory.getLogger(AuthController::class.java)


    @PostMapping("/signup")
    fun signup(@RequestBody signUpRequest: SignUpRequest): ResponseEntity<CommonResponse<String>> {
        try {
            val user: User = authentictionSerives.signup(signUpRequest)
            val message = CommonResponse<String>(data = null, message = ("A verification email has been sent to your email address."), status = true)
            return ResponseEntity<CommonResponse<String>>(message, HttpStatus.ACCEPTED)
        } catch (e: BadApiRequest) {
            e.printStackTrace()
            val message = CommonResponse<String>(message = e.message ?: "user already exists...", status = false, data = null)
            return ResponseEntity<CommonResponse<String>>(message, HttpStatus.BAD_REQUEST)
        } catch (e: Exception) {
            e.printStackTrace()
            val message =
                CommonResponse<String>(message = "Wrong Credential...", status = false, data = null)
            return ResponseEntity<CommonResponse<String>>(message, HttpStatus.BAD_REQUEST)
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
    fun signIn(@RequestBody signinRequest: SigninRequest?): ResponseEntity<CommonResponse<UserSession>> {
        return try {
            val response = authentictionSerives.signin(signinRequest)
            val message=
                CommonResponse<UserSession>(data = response, message = "Login successfully...", status = true)
                ResponseEntity<CommonResponse<UserSession>>(message, HttpStatus.ACCEPTED)

        } catch (e: Exception) {
            when(e)
            {
                is BadApiRequest->{
                    val message=
                        CommonResponse<UserSession>(data = null, message = e.message.toString(), status = false)
                    ResponseEntity<CommonResponse<UserSession>>(message, HttpStatus.BAD_REQUEST)

                }
                is BadCredentialsException->{
                    val message =
                        CommonResponse<UserSession>(data = null, message = "Invalid Password!", status = false)
                    ResponseEntity<CommonResponse<UserSession>>(message, HttpStatus.BAD_REQUEST)
                }
                else->
                {
                    val message =
                        CommonResponse<UserSession>(data = null, message = e.message  ?: "Something went wrong!", status = false)
                    ResponseEntity<CommonResponse<UserSession>>(message, HttpStatus.BAD_REQUEST)
                }
            }

        }
    }


    @PostMapping("/refresh")
    fun refresh(@RequestHeader("Authorization") request: String): ResponseEntity<CommonResponse<UserSession>?> {
        return try {
            val response: UserSession? = authentictionSerives?.refreshToken(request.substring(7))
            ResponseEntity.ok(
                CommonResponse<UserSession>(data = response, message = "successful", status = true)
            )
        } catch (e: Exception) {
            ResponseEntity.badRequest()
                .body(CommonResponse<UserSession>(data = null, message = "something went wrong...", status = false))
        }
    }




    @PostMapping("/signin-with-google")
    fun signInWithGoogle(@RequestBody request:SignInWithGoogleRequest): ResponseEntity<CommonResponse<UserSession>> {
        val googleUser = authentictionSerives.verifyGoogleIdToken(request.idToken)
            ?: return ResponseEntity.badRequest().body(CommonResponse(message = "Unable to authenticate this account!", status = false, data = null))
        if(!googleUser.email_verified){
            return ResponseEntity.badRequest().body(CommonResponse(message = "Account is not verified!", status = false, data = null))
        }
        return try {
            val userSession= authentictionSerives.signInWithGoogle(googleUser)
            ResponseEntity.ok().body(CommonResponse(message = "Signed in successfully", status = true,data = userSession))
        }catch (e:BadApiRequest){
            e.printStackTrace()
            ResponseEntity.badRequest().body(CommonResponse(message = e.message?:"Something went wrong", status = false,data = null))
        }catch (e:Exception){
            e.printStackTrace()
            ResponseEntity.badRequest().body(CommonResponse(message = "Something went wrong", status = false,data = null))
        }

    }


    @PutMapping("/change-password")
    fun changePassword(@RequestHeader(name = "Authorization") token: String,@RequestBody changePasswordRequest: ChangePasswordRequest):ResponseEntity<CommonResponse<Nothing>>{
        try {
            val email = jwtServices.extractEmail(token.substring(7))

            val message= authentictionSerives.changePassword(newPassword = changePasswordRequest.newPassword, oldPassword = changePasswordRequest.oldPassword, email)
            return ResponseEntity.ok().body(CommonResponse(message = message,status = true))

        }catch (e:Exception){
            when (e){
                is BadCredentialsException->{
                    return ResponseEntity.badRequest().body(CommonResponse(message = "incorrect old password.!" , status = false))
                }else->{
                return ResponseEntity.badRequest().body(CommonResponse(message = e.message.toString() , status = false))
            }
            }

        }
    }

}
