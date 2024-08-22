package com.Inderjeet.News.rest.dto.auth

data class ChangePassword(val password: String,
                          val email:String,val otp:Int)
