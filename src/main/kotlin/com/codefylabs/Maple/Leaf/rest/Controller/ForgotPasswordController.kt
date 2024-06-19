package com.codefylabs.Maple.Leaf.rest.Controller

import com.codefylabs.Maple.Leaf.business.gateway.EmailServices
import com.codefylabs.Maple.Leaf.business.gateway.ForgotServices
import com.codefylabs.Maple.Leaf.business.gateway.JWTServices
import com.codefylabs.Maple.Leaf.persistance.ForgotPassword
import com.codefylabs.Maple.Leaf.persistance.User
import com.codefylabs.Maple.Leaf.persistance.UserRepositoryJpa
import com.codefylabs.Maple.Leaf.rest.dto.ChangePassword
import com.codefylabs.Maple.Leaf.rest.dto.MailBody
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.util.*


@RestController
@RequestMapping("/forgotPassword")
class ForgotPasswordController(
    val emailServices: EmailServices,
    val forgotServices: ForgotServices,
    val passwordEncoder: PasswordEncoder,
    val userRepository: UserRepositoryJpa,
    val jwtServices:JWTServices
) {

val logger= LoggerFactory.getLogger(ForgotPasswordController::class.java)
    @PostMapping("/verifyMail")
    fun verifyEmail(@RequestHeader("Authorization") request: String): ResponseEntity<String> {
        val email = jwtServices?.extractUserName(request.substring(7))
            logger.info("this is the name "+email.toString()+" ")
             val user: User? =userRepository.findByEmail(email)?.orElseThrow { RuntimeException("please provide an valid email...") }
        val otp = OtpGenerator()
        val mailBody: MailBody = MailBody(
            to = email,
            text = "This is the otp of your account forgot password...$otp",
            subject = "otp for forgot password request..."
        )
        val fp: ForgotPassword = ForgotPassword(
            fid = user?.id ?: 0,
            otp = otp,
            expirationTime = Date(System.currentTimeMillis() + 70 * 1000),
            user = user
        )
        emailServices.sendSimpleMessage(mailBody)
        forgotServices.saveOtp(fp)
        return ResponseEntity.ok("Email sent for verification...")
    }


    @PostMapping("/verifyOtp")
    fun verifyOtp(@RequestHeader("Authorization") request:String,@RequestParam(value = "otp") otp: Int?): ResponseEntity<String> {
        val email = jwtServices?.extractUserName(request.substring(7))
        val user: User? =
            userRepository.findByEmail(email)?.orElseThrow { RuntimeException("please provide an valid email...") }
        val fp: ForgotPassword? = forgotServices.otpFindByOtpAndUser(otp, user)
        if (fp?.expirationTime?.before(Date.from(Instant.now())) == true) {
            if (fp != null) {
                forgotServices.deleteOtpById(fp.fid)
            }
            return ResponseEntity("Otp has expired...", HttpStatus.EXPECTATION_FAILED)
        }
        if (fp != null) {
            forgotServices.deleteOtpById(fp.fid)
        }
        return ResponseEntity.ok("otp verifed...")
    }


    @PostMapping("/changePassword")
    fun changePasswordHandler(
        @RequestParam(name = "password") changePassword: ChangePassword,@RequestHeader("Authorization") request:String
    ): ResponseEntity<String> {
        if (changePassword.password != changePassword.repeatPassword) {
            return ResponseEntity("Please enter the password again...", HttpStatus.EXPECTATION_FAILED)
        }
        val email = jwtServices?.extractUserName(request.substring(7))
        val password = passwordEncoder.encode(changePassword.password)
        userRepository.updatePassword(email, password)
        return ResponseEntity.ok("Password has been changed...")
    }

    private fun OtpGenerator(): Int {
        val random = Random()
        return random.nextInt(100000, 999999)
    }
}

