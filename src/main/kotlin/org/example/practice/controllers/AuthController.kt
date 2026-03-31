package org.example.practice.controllers

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.example.practice.dto.LoginRequest
import org.example.practice.dto.LoginResponse
import org.example.practice.dto.RegisterRequest
import org.example.practice.services.AuthService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {

    private val log = LoggerFactory.getLogger(AuthController::class.java)

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest, response: HttpServletResponse): ResponseEntity<LoginResponse> {
        log.info("Login attempt for user: {}", request.username)
        return ResponseEntity.ok(authService.login(request, response))
    }

    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<LoginResponse> {
        log.info("Registration attempt for user: {}", request.username)
        return ResponseEntity.ok(authService.register(request))
    }

    @PostMapping("/refresh")
    fun refresh(request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<LoginResponse> {
        val refreshToken = request.cookies?.find { it.name == "refreshToken" }?.value
            ?: return ResponseEntity.status(401).build()
        return ResponseEntity.ok(authService.refresh(refreshToken, response))
    }

    @PostMapping("/logout")
    fun logout(request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<Void> {
        val refreshToken = request.cookies?.find { it.name == "refreshToken" }?.value
        if (refreshToken != null) {
            authService.logout(refreshToken, response)
        }
        return ResponseEntity.noContent().build()
    }
}