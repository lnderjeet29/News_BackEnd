package com.codefylabs.Maple.Leaf.persistance

import jakarta.transaction.Transactional
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.util.*


interface UserRepositoryJpa : JpaRepository<User?, Int?> {

    fun findByEmail(email: String?): Optional<User>
    fun findByVerificationToken(token: String): Optional<User>?
    fun findByName(name:String?): Optional<List<User>>




    @Transactional
    @Modifying
    @Query("update User u set u.password = ?2 where u.email = ?1")
    fun updatePassword(email: String?, password: String?)
}
