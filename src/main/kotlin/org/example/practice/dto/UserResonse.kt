package org.example.practice.dto

data class UserResponse(
    val id: Long?,
    val username: String?,
    val email: String? = null,
    val role: String? = null
)