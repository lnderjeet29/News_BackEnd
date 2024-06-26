package com.codefylabs.Maple.Leaf.rest.dto.auth

data class ChangePassword(val password: String,
                          val email:String,val otp:Int)
