package org.example.practice.services

import org.example.practice.config.JwtConfig
import org.example.practice.model.RefreshToken
import org.example.practice.model.User
import org.example.practice.repositories.RefreshTokenRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
class RefreshTokenService(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtConfig: JwtConfig
) {
    fun generate(user: User): RefreshToken =
        refreshTokenRepository.save(
            RefreshToken(
                token = UUID.randomUUID().toString(),
                user = user,
                expiresAt = Instant.now().plusSeconds(jwtConfig.refreshDuration)
            )
        )

    fun validate(token: String): RefreshToken {
        val refreshToken = refreshTokenRepository.findByToken(token)
            ?: throw IllegalArgumentException("Invalid refresh token")
        if (refreshToken.revoked)
            throw IllegalArgumentException("Refresh token has been revoked")
        if (refreshToken.expiresAt.isBefore(Instant.now()))
            throw IllegalArgumentException("Refresh token has expired")
        return refreshToken
    }

    @Transactional
    fun revoke(token: String) {
        val refreshToken = refreshTokenRepository.findByToken(token) ?: return
        refreshToken.revoked = true
    }
}