package com.codefylabs.Maple.Leaf.persistence.entities

import jakarta.persistence.*
import java.util.*


@Entity
@Table(name = "forgotPassword")
data class ForgotPassword (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val fid: Int = 0,  // Default value for Hibernate

    @Column(nullable = false)
    val otp: Int,

    @Column(nullable = false)
    val expirationTime: Date,

    @OneToOne
    val user: User? = null  // Default null for Hibernate
) {
    // Hibernate requires a no-arg constructor
    constructor() : this(
        otp = 0,
        expirationTime = Date()
    )
}
