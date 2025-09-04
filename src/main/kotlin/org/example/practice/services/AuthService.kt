package org.example.practice.services

import jakarta.transaction.Transactional
import org.example.practice.dto.LoginRequest
import org.example.practice.dto.LoginResponse
import org.example.practice.dto.RegisterRequest
import org.example.practice.dto.UserResponse
import org.example.practice.exception.InvalidCredentialsException
import org.example.practice.exception.UserAlreadyExistsException
import org.example.practice.exception.UserNotFoundException
import org.example.practice.model.Role
import org.example.practice.model.User
import org.example.practice.repositories.UserRepository
import org.example.practice.utils.CustomUserDetails
import org.example.practice.utils.JwtUtil
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
//TODO: implement OAUTH2 with JWT, add logging, add refresh token mechanism
@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {
    @Transactional
    fun register(request: RegisterRequest): LoginResponse {
        userRepository.findByUsername(request.username)?.let {
            throw UserAlreadyExistsException("Username '${request.username}' is already registered")
        }

        userRepository.findByEmail(request.email)?.let {
            throw UserAlreadyExistsException("Email '${request.email}' is already registered")
        }

        val user = userRepository.save(
            User(
                username = request.username,
                email = request.email,
                password = passwordEncoder.encode(request.password),
                roles = mutableSetOf(Role.ROLE_USER)
            )
        )

        val token = jwtUtil.generateToken(CustomUserDetails(user))
        return LoginResponse(token, UserResponse(id = user.id, username = user.username))
    }

    fun login(request: LoginRequest): LoginResponse {
        val user = userRepository.findByUsername(request.username)
            ?: throw UserNotFoundException("User with username '${request.username}' does not exist")

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw InvalidCredentialsException("Invalid credentials provided")
        }

        val token = jwtUtil.generateToken(CustomUserDetails(user))
        return LoginResponse(token, UserResponse(id = user.id, username = user.username))
    }

}