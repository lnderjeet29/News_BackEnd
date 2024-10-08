package com.codefylabs.Maple.Leaf.persistance.services

import com.Inderjeet.News.business.gateway.ForgotServices
import com.Inderjeet.News.persistence.entities.AuthProvider
import com.Inderjeet.News.persistence.entities.ForgotPassword
import com.Inderjeet.News.persistence.entities.User
import com.Inderjeet.News.persistence.repository.ForgotPasswordRepository
import com.Inderjeet.News.persistence.repository.UserRepositoryJpa
import com.Inderjeet.News.rest.ExceptionHandler.BadApiRequest
import com.Inderjeet.News.rest.dto.others.MailBody
import lombok.RequiredArgsConstructor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
@RequiredArgsConstructor
class ForgotServicesImp(val emailServices: com.Inderjeet.News.business.gateway.EmailServices,
                        val forgotPasswordRepository: ForgotPasswordRepository,
                        val userRepository: UserRepositoryJpa
): ForgotServices {


   val logger:Logger=LoggerFactory.getLogger(ForgotServicesImp::class.java)
    override fun sendOtpToEmail(email:String?){
        logger.info(email)
        val user = userRepository.findByEmail(email).orElseThrow{ BadApiRequest("Email not found!") }
        if (user.isBlocked) {
            throw BadApiRequest("Your account has been blocked. Please contact support for further assistance.!")
        }
        if (!user.enabled) {
            throw BadApiRequest("Your email address is not verified.!")
        }
        if (user.authProvider=== AuthProvider.GOOGLE) {
            throw BadApiRequest("This email is connected to Google Sign-In. Please continue with Google.")
        }
            val otp = GenerateOtp()
            val mailBody: MailBody = MailBody(
                to = email,
                text = "Here is your OTP for account password reset: $otp",
                subject = "Your OTP for Account Password Reset"
            )
            val fp: ForgotPassword = ForgotPassword(
                fid = user?.id ?: 0,
                otp = otp,
                expirationTime = Date(System.currentTimeMillis() + 10 * 60 * 1000),
                user = user
            )
        emailServices.sendSimpleMessage(mailBody)
        try {
            forgotPasswordRepository.save(fp)
        } catch (e: Exception) {
            val u: Optional<ForgotPassword> = forgotPasswordRepository.findByUser(fp.user)

            forgotPasswordRepository.deleteById(u.get().fid)
            forgotPasswordRepository.save(fp)
        }

    }

    private fun GenerateOtp(): Int {
        val random = Random()
        return random.nextInt(100000, 999999)
    }

    override fun deleteOtpById(fid: Int) {
        forgotPasswordRepository.deleteById(fid)
    }

    override fun otpFindByOtpAndUser(otp: Int?, user: User?): ForgotPassword? {
        return forgotPasswordRepository.findByOtpAndUser(otp, user)?.orElseThrow {
            RuntimeException(
                "Invalid OTP...${user?.email}"
            )
        }
    }
}