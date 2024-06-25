package com.codefylabs.Maple.Leaf.persistance.Implementation


import com.codefylabs.Maple.Leaf.business.gateway.ForgotServices
import com.codefylabs.Maple.Leaf.persistance.ForgotPassword
import com.codefylabs.Maple.Leaf.persistance.ForgotPasswordRepository
import com.codefylabs.Maple.Leaf.persistance.User
import org.springframework.stereotype.Service

@Service
class ForgotServicesImp(val forgotPasswordRepository: ForgotPasswordRepository): ForgotServices {

    override fun saveOtp(fp: ForgotPassword) {
        forgotPasswordRepository.save(fp)
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