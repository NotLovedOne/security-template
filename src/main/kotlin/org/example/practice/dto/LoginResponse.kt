package org.example.practice.dto

data class LoginResponse(
    val token: String,
    val user: UserResponse
)