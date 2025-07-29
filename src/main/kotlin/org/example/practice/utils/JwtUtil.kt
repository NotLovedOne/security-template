package org.example.practice.utils

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.example.practice.config.JwtConfig
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtil(
    jwtConfig: JwtConfig
) {
    private val secretKey: SecretKey =
        Keys.hmacShaKeyFor(jwtConfig.secret.toByteArray(StandardCharsets.UTF_8))
    private val expirationMs: Long = jwtConfig.expiration * 1000

    fun generateToken(userDetails: UserDetails): String {
        val now = Date()
        val exp = Date(now.time + expirationMs)
        return Jwts.builder()
            .subject(userDetails.username)
            .issuedAt(now)
            .expiration(exp)
            .signWith(secretKey)
            .compact()
    }

    fun extractUsername(token: String): String? = try {
        val claims = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
        claims.subject
    } catch (_: Exception) {
        null
    }

    private fun isExpired(token: String): Boolean = try {
        val exp = Jwts.parser().verifyWith(secretKey).build()
            .parseSignedClaims(token).payload.expiration
        exp.before(Date())
    } catch (_: Exception) {
        true
    }

    fun validate(token: String, userDetails: UserDetails): Boolean {
        val u = extractUsername(token) ?: return false
        if (u != userDetails.username) return false
        return !isExpired(token)
    }
}
