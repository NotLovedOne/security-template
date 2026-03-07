package org.example.practice.controllers

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.example.practice.dto.LoginRequest
import org.example.practice.dto.LoginResponse
import org.example.practice.dto.RegisterRequest
import org.example.practice.services.AuthService
import org.example.practice.utils.JwtUtil
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
    private val jwtService: JwtUtil
) {

    private val log = LoggerFactory.getLogger(AuthController::class.java)

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest, response: HttpServletResponse): ResponseEntity<LoginResponse> {
        log.info("Login attempt for user: {}", request.username)
        val response = authService.login(request, response)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<LoginResponse> {
        log.info("Registration attempt for user: {}", request.username)
        val response = authService.register(request)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/me")
    fun me(request: HttpServletRequest): ResponseEntity<String> {
        val token = request.cookies?.find { it.name == "token" }?.value?: return ResponseEntity.status(401).build()
        val user = jwtService.extractUsername(token)
        log.info("User info requested for user: {}, {}", user, token)
        return ResponseEntity.ok(user)
    }

}