package com.codefylabs.Maple.Leaf.business.gateway

import com.codefylabs.Maple.Leaf.persistence.entities.ForgotPassword
import com.codefylabs.Maple.Leaf.persistence.entities.User


interface ForgotServices {
    fun sendOtpToEmail(email:String?)
    fun deleteOtpById(fid:Int)
    fun otpFindByOtpAndUser(otp:Int?,user: User?): ForgotPassword?
}