package org.example.practice.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class JwtConfig {
    @Value("\${jwt.secret}")
    var secret: String? = null
    @Value("\${jwt.duration}")
    var expiration: Int? = null
}