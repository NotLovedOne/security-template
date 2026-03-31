package org.example.practice.services

import jakarta.servlet.http.HttpServletResponse
import jakarta.transaction.Transactional
import org.example.practice.config.JwtConfig
import org.example.practice.dto.LoginRequest
import org.example.practice.dto.LoginResponse
import org.example.practice.dto.RegisterRequest
import org.example.practice.dto.UserResponse
import org.example.practice.model.Role
import org.example.practice.model.User
import org.example.practice.repositories.UserRepository
import org.example.practice.utils.CustomUserDetails
import org.example.practice.utils.JwtUtil
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
    private val jwtConfig: JwtConfig,
    private val refreshTokenService: RefreshTokenService
) {
    @Transactional
    fun register(request: RegisterRequest): LoginResponse {
        if (userRepository.findByEmail(request.email) != null || userRepository.findByUsername(request.username) != null) {
            throw IllegalArgumentException("Username ${request.username} is already registered")
        }

        val user = userRepository.save(
            User(
                username = request.username,
                email = request.email,
                password = passwordEncoder.encode(request.password),
                roles = mutableSetOf(Role.ROLE_USER)
            )
        )

        return LoginResponse(UserResponse(id = user.id, username = user.username))
    }

    @Transactional
    fun login(request: LoginRequest, response: HttpServletResponse): LoginResponse {
        val user = userRepository.findByUsername(request.username)
            ?: throw IllegalArgumentException("Invalid credentials")

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw IllegalArgumentException("Invalid credentials")
        }

        val accessToken = jwtUtil.generateToken(CustomUserDetails(user))
        val refreshToken = refreshTokenService.generate(user)

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie(accessToken).toString())
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie(refreshToken.token).toString())

        return LoginResponse(UserResponse(id = user.id, username = user.username))
    }

    @Transactional
    fun refresh(refreshTokenValue: String, response: HttpServletResponse): LoginResponse {
        val refreshToken = refreshTokenService.validate(refreshTokenValue)
        val user = refreshToken.user

        refreshTokenService.revoke(refreshTokenValue)
        val newRefreshToken = refreshTokenService.generate(user)
        val newAccessToken = jwtUtil.generateToken(CustomUserDetails(user))

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie(newAccessToken).toString())
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie(newRefreshToken.token).toString())

        return LoginResponse(UserResponse(id = user.id, username = user.username))
    }

    fun logout(refreshTokenValue: String, response: HttpServletResponse) {
        refreshTokenService.revoke(refreshTokenValue)
        response.addHeader(HttpHeaders.SET_COOKIE, clearCookie("token").toString())
        response.addHeader(HttpHeaders.SET_COOKIE, clearCookie("refreshToken").toString())
    }

    private fun accessTokenCookie(token: String): ResponseCookie =
        ResponseCookie.from("token", token)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(jwtConfig.expiration)
            .sameSite("Strict")
            .build()

    private fun refreshTokenCookie(token: String): ResponseCookie =
        ResponseCookie.from("refreshToken", token)
            .httpOnly(true)
            .secure(true)
            .path("/auth")
            .maxAge(jwtConfig.refreshDuration)
            .sameSite("Strict")
            .build()

    private fun clearCookie(name: String): ResponseCookie =
        ResponseCookie.from(name, "")
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(0)
            .sameSite("Strict")
            .build()
}