package org.example.practice.controllers

import org.example.practice.dto.LoginRequest
import org.example.practice.dto.LoginResponse
import org.example.practice.dto.RegisterRequest
import org.example.practice.services.AuthService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController(value = "auth/")
class AuthController(
    private val authService: AuthService
) {

    private val log = LoggerFactory.getLogger(AuthController::class.java)

    @PostMapping("/login")
    fun login(request: LoginRequest): LoginResponse {
        return authService.login(request)
    }

    @PostMapping("/register")
    fun register(request: RegisterRequest): LoginResponse {
        return authService.register(request)
    }

}