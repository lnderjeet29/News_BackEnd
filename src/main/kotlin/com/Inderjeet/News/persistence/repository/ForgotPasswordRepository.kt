package com.Inderjeet.News.persistence.repository

import com.Inderjeet.News.persistence.entities.ForgotPassword
import com.Inderjeet.News.persistence.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ForgotPasswordRepository : JpaRepository<ForgotPassword?, Int?> {

    fun findByUser(user: User?): Optional<ForgotPassword>
    @Query("select fp from ForgotPassword fp where fp.otp=?1 and fp.user=?2")
    fun findByOtpAndUser(otp: Int?, user: User?): Optional<ForgotPassword?>?
}
