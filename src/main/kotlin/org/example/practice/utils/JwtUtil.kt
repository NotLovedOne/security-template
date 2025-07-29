package org.example.practice.utils

import io.jsonwebtoken.Jwts
import org.example.practice.config.JwtConfig
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureAlgorithm

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtUtil(private val jwtConfig: JwtConfig? = null) {
    private val secret = Keys.hmacShaKeyFor(jwtConfig?.secret?.toByteArray())

    open fun generateToken(userDetails: CustomUserDetails): String {
        val now = Date()
        val exp = Date(now.time + jwtConfig?.expiration!! * 1000)
        return Jwts.builder().subject(userDetails.username).issuedAt(now).expiration(exp).signWith(secret).compact()

    }

    fun extractUsername(token: String): String? {
        return try {
            val claims = Jwts.parser().verifyWith(secret).build().parseSignedClaims(token).payload.subject
            claims
        }
        catch (e: Exception){
            null
        }
    }

    fun isExpired(token: String): Boolean{
        return try {
            val exp = Jwts.parser().verifyWith(secret).build().parseSignedClaims(token).payload.expiration
            exp.before(Date())
        }
        catch(e: Exception){
            true
        }
    }

    fun validate(token: String, userDetails: UserDetails): Boolean = extractUsername(token)?.isNotBlank()!! && !isExpired(token)

}