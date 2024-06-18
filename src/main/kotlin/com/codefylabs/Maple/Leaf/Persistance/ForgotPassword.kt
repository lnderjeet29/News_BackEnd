package com.codefylabs.Maple.Leaf.persistance

import jakarta.persistence.*
import java.util.*


@Entity
@Table(name = "forgotPassword")
data class ForgotPassword (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val fid: Int,

    @Column(nullable = false)
    val otp: Int,

    @Column(nullable = false)
    val expirationTime: Date,

    @OneToOne
    val user: User?
)
