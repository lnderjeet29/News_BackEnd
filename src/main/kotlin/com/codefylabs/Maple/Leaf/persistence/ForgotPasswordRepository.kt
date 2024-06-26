package com.codefylabs.Maple.Leaf.persistance

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*


interface ForgotPasswordRepository : JpaRepository<ForgotPassword?, Int?> {

    fun findByUser(user: User?): Optional<ForgotPassword>
    @Query("select fp from ForgotPassword fp where fp.otp=?1 and fp.user=?2")
    fun findByOtpAndUser(otp: Int?, user: User?): Optional<ForgotPassword?>?
}
