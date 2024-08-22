package com.Inderjeet.News.business.gateway

import com.Inderjeet.News.persistence.entities.ForgotPassword
import com.Inderjeet.News.persistence.entities.User

interface ForgotServices {
    fun sendOtpToEmail(email:String?)
    fun deleteOtpById(fid:Int)
    fun otpFindByOtpAndUser(otp:Int?,user: User?): ForgotPassword?
}