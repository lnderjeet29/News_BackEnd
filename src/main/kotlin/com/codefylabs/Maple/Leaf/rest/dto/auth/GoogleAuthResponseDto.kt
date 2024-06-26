package com.codefylabs.Maple.Leaf.rest.dto.auth

data class GoogleAuthResponseDto(
    val iss: String,
    val azp: String,
    val aud: String,
    val sub: String,
    val hd: String?,
    val email: String,
    val email_verified: Boolean,
    val at_hash: String?,
    val nonce: String?,
    val name: String,
    val picture: String,
    val given_name: String,
    val family_name: String,
    val iat: Long,
    val exp: Long,
    val alg: String,
    val kid: String,
    val typ: String
)