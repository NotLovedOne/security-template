package org.example.practice.services

import org.example.practice.repositories.UserRepository
import org.example.practice.utils.CustomUserDetails
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(private val repository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        val user = repository.findByUsername(username!!)
            ?: throw Exception("User with username $username not found")
        return CustomUserDetails(user)
    }
}