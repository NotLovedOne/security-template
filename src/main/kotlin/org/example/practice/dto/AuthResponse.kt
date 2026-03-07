package org.example.practice.dto

data class AuthResponse (
    val token: String,
    val user: UserResponse
)