package com.codefylabs.Maple.Leaf.rest.dto

data class ChangePassword(val password: String,
                          val email:String,val otp:Int)
