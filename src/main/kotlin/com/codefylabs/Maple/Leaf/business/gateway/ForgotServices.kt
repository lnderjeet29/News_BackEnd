package com.codefylabs.Maple.Leaf.business.gateway

import com.codefylabs.Maple.Leaf.persistance.ForgotPassword
import com.codefylabs.Maple.Leaf.persistance.User


interface ForgotServices {
    fun sendOtpToEmail(email:String?)
    fun deleteOtpById(fid:Int)
    fun otpFindByOtpAndUser(otp:Int?,user: User?):ForgotPassword?
}