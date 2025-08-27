package org.example.practice.utils

import org.example.practice.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails (private val user : User) : UserDetails {


    override fun getAuthorities(): Collection<GrantedAuthority> {
        return user.roles.map{
            SimpleGrantedAuthority(it.name)
        }
    }

    override fun getPassword(): String? {
        return user.password
    }

    override fun getUsername(): String? {
        return user.username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }
    override fun isAccountNonLocked(): Boolean {
        return true
    }
    override fun isCredentialsNonExpired(): Boolean {
        return true
    }
    override fun isEnabled(): Boolean {
        return true
    }

    fun dtoToUser(): CustomUserDetails {
        return CustomUserDetails(user)
    }

}