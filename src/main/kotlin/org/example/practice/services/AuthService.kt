package org.example.practice.services

import jakarta.servlet.http.HttpServletRequest
import jakarta.transaction.Transactional
import org.example.practice.dto.LoginRequest
import org.example.practice.dto.LoginResponse
import org.example.practice.dto.RegisterRequest
import org.example.practice.dto.UserResponse
import org.example.practice.model.User
import org.example.practice.repositories.UserRepository
import org.example.practice.utils.CustomUserDetails
import org.example.practice.utils.JwtUtil
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
    util: JwtUtil
) {
    @Transactional
    fun register(request: RegisterRequest): LoginResponse{
        if (userRepository.findByEmail(request.email) != null || userRepository.findByUsername(request.username) != null) {
            throw IllegalArgumentException("Username ${request.username} is already registered")
        }

        val user = userRepository.save(
            User(
                username = request.username,
                email = request.email,
                password = passwordEncoder.encode(request.password),
                roles = mutableSetOf(org.example.practice.model.Role.ROLE_USER)
            )
        )

        val token = jwtUtil.generateToken(CustomUserDetails(user))
        return LoginResponse(token, UserResponse(id = user.id, username =user.username))
    }

    fun login(request: LoginRequest): LoginResponse {
        if(userRepository.findByUsername(request.username) == null) {
            throw IllegalArgumentException("Username ${request.username} does not exist")
        }
        val user = userRepository.findByUsername(request.username)!!

        if(!passwordEncoder.matches(request.password, user.password)) {
            throw IllegalArgumentException("Password ${user.password} does not match required password")
        }
        val token = jwtUtil.generateToken(CustomUserDetails(user))
        return LoginResponse(token, UserResponse(id = user.id, username =user.username))

    }

}