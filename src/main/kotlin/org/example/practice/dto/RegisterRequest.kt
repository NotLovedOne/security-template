package org.example.practice.dto

import org.example.practice.model.Role
import org.example.practice.model.User

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val role: Role = Role.ROLE_USER
) {
    fun dtoToUser(): User {
        return User(username = this.username, password = this.password)
    }
}
