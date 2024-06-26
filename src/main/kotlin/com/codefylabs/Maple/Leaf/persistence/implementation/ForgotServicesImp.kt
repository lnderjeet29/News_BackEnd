package com.codefylabs.Maple.Leaf.persistance.Implementation


import com.codefylabs.Maple.Leaf.business.gateway.EmailServices
import com.codefylabs.Maple.Leaf.business.gateway.ForgotServices
import com.codefylabs.Maple.Leaf.persistance.ForgotPassword
import com.codefylabs.Maple.Leaf.persistance.ForgotPasswordRepository
import com.codefylabs.Maple.Leaf.persistance.User
import com.codefylabs.Maple.Leaf.persistance.UserRepositoryJpa
import com.codefylabs.Maple.Leaf.rest.ExceptionHandler.BadApiRequest
import com.codefylabs.Maple.Leaf.rest.dto.MailBody
import lombok.RequiredArgsConstructor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
@RequiredArgsConstructor
class ForgotServicesImp(val emailServices: EmailServices,
                        val forgotPasswordRepository: ForgotPasswordRepository,
                        val userRepository: UserRepositoryJpa): ForgotServices {


   val logger:Logger=LoggerFactory.getLogger(ForgotServicesImp::class.java)
    override fun sendOtpToEmail(email:String?){
        logger.info(email)
        val user = userRepository.findByEmail(email).orElseThrow{BadApiRequest("Email not found!")}

            val otp = generateOtp()
            val mailBody: MailBody = MailBody(
                to = email,
                text = "Here is your OTP for account password reset: $otp",
                subject = "Your OTP for Account Password Reset"
            )
            val fp: ForgotPassword = ForgotPassword(
                fid = user?.id ?: 0,
                otp = otp,
                expirationTime = Date(System.currentTimeMillis() + 70 * 1000),
                user = user
            )
            try {
                val us=forgotPasswordRepository.findByUser(fp.user)
                if(us!=null){
                    forgotPasswordRepository.deleteById(us.get().fid)
                }
                emailServices.sendSimpleMessage(mailBody)
                forgotPasswordRepository.save(fp)
            } catch (e: Exception) {
                throw RuntimeException("Fail to send or save email!")
            }

    }

    private fun generateOtp(): Int {
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