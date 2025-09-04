package org.example.practice.controllers

import jakarta.validation.Valid
import org.example.practice.dto.ApiResponse
import org.example.practice.dto.LoginRequest
import org.example.practice.dto.LoginResponse
import org.example.practice.dto.RegisterRequest
import org.example.practice.services.AuthService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService
) {
    private val log = LoggerFactory.getLogger(AuthController::class.java)

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<ApiResponse<LoginResponse>> {
        log.info("Login attempt for user: {}", request.username)
        val response = authService.login(request)
        return ResponseEntity.ok(
            ApiResponse.success(
                data = response,
                message = "Login successful",
                status = HttpStatus.OK.value()
            )
        )
    }

    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<ApiResponse<LoginResponse>> {
        log.info("Registration attempt for user: {}", request.username)
        val response = authService.register(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.success(
                data = response,
                message = "User registered successfully",
                status = HttpStatus.CREATED.value()
            )
        )
    }

}