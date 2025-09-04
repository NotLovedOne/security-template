package org.example.practice.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.example.practice.model.Role
import org.example.practice.model.User

data class RegisterRequest(
    @field:NotBlank(message = "Username is required")
    @field:Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @field:Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "Username can only contain letters, numbers, underscores and hyphens")
    val username: String,

    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email should be valid")
    @field:Size(max = 100, message = "Email must not exceed 100 characters")
    val email: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @field:Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
        message = "Password must contain at least one digit, one lowercase, one uppercase letter, one special character, and no whitespace"
    )
    val password: String,

    val role: Role = Role.ROLE_USER
)
