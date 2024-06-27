package com.codefylabs.Maple.Leaf.persistence.implementation

import com.codefylabs.Maple.Leaf.business.gateway.JWTServices
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*


@Service
class JWTServicesImpl : JWTServices {


    private fun <T> extractClaim(token: String, claimsResolvers: (Claims) -> T): T {
        val claims: Claims = extractAllClaim(token)
        return claimsResolvers.invoke(claims)
    }

    private fun extractAllClaim(token: String): Claims {
        return Jwts.parserBuilder().setSigningKey(signKey).build().parseClaimsJws(token).getBody()
    }


    private val signKey: Key
        private get() {
            val key: ByteArray =
                Decoders.BASE64.decode("bXkgbmFtZSBpcyBpbmRlcmplZXQgeWFkYXYgbmljZSB0byBtZWV0IHlvdSBwbGVhc2UgdGFrZSB5b3VyIGtleSBhbmQga2V5IGlzIGplZXQ")
            return Keys.hmacShaKeyFor(key)
        }


    private fun isTokenExpired(token: String): Boolean {
        return extractClaim<Date>(token, Claims::getExpiration).before(Date())
    }


    override fun extractUserName(token: String?): String? {
        return token?.let { extractClaim<String>(it, Claims::getSubject) }
    }

    override fun generateToken(userDetails: UserDetails?): String {
        return Jwts.builder().setSubject(userDetails?.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 ))
            .signWith(signKey, SignatureAlgorithm.HS256)
            .compact()
    }

    override fun isTokenValid(token: String?, userDetails: UserDetails?): Boolean {
        val username = extractUserName(token)
        return username == userDetails?.username && !token?.let { isTokenExpired(it) }!!
    }

    override fun generateRefreshToken(extraClaims: Map<String?, Any?>?, userDetails: UserDetails?): String {
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails?.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 25))
            .signWith(signKey, SignatureAlgorithm.HS256)
            .compact()
    }
}
