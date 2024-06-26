package com.codefylabs.Maple.Leaf.rest.controller

import com.codefylabs.Maple.Leaf.business.gateway.ForgotServices
import com.codefylabs.Maple.Leaf.persistance.ForgotPassword
import com.codefylabs.Maple.Leaf.persistance.User
import com.codefylabs.Maple.Leaf.persistance.UserRepositoryJpa
import com.codefylabs.Maple.Leaf.rest.ExceptionHandler.BadApiRequest
import com.codefylabs.Maple.Leaf.rest.dto.ChangePassword
import com.codefylabs.Maple.Leaf.rest.dto.ForgotResponseDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.util.*


@RestController
@RequestMapping("/api/v1/forgot-password")
class ForgotPasswordController(

    val forgotServices: ForgotServices,
    val passwordEncoder: PasswordEncoder,
    val userRepository: UserRepositoryJpa
) {

    val logger:Logger= LoggerFactory.getLogger(ForgotPasswordController::class.java)

    @PostMapping("/send-otp")
    fun forgotPassword(@RequestParam(value = "email") email: String?): ResponseEntity<ForgotResponseDto> {
        try {
            forgotServices.sendOtpToEmail(email)
        } catch (e: Exception) {
            when(e){
                is BadApiRequest->{
                    e.printStackTrace()
                    return ResponseEntity(ForgotResponseDto(status = false, message = "Email not found!"),HttpStatus.NOT_FOUND)
                }
                else->{
                    return ResponseEntity(ForgotResponseDto(status = false, message = "Something went wrong!"),HttpStatus.NOT_FOUND)
                }
            }

        }

        val message=ForgotResponseDto(status = true, message = "OTP send to email.")
        return ResponseEntity.ok(message)
    }


    @PostMapping("/reset")
    fun verifyOtp(@RequestBody changePassword: ChangePassword): ResponseEntity<ForgotResponseDto> {
        val user: User? =
            userRepository.findByEmail(changePassword.email)?.orElseThrow { RuntimeException("please provide an valid email...") }
        val fp: ForgotPassword? = forgotServices.otpFindByOtpAndUser(changePassword.otp, user)
        if (fp?.expirationTime?.before(Date.from(Instant.now())) == true) {
            if (fp != null) {
                forgotServices.deleteOtpById(fp.fid)
            }

            val message=ForgotResponseDto(status = false, message = "Otp has expired...")
            return ResponseEntity(message,HttpStatus.EXPECTATION_FAILED)
        }
        if (fp != null) {
            forgotServices.deleteOtpById(fp.fid)
        }
        changePasswordHandler(changePassword.password,changePassword.email)
        val message=ForgotResponseDto(status = true, message = "Password changed successfully")
        return ResponseEntity.ok(message)
    }



    fun changePasswordHandler(
        password:String,
       email: String?
    ) {
        val password = passwordEncoder.encode(password)
        userRepository.updatePassword(email, password)

    }


}

