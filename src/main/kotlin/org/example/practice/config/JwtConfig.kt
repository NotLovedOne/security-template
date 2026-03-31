package org.example.practice.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class JwtConfig {
    @Value("\${jwt.secret}")
    lateinit var secret: String

    @Value("\${jwt.duration}")
    var expiration: Long = 3600

    @Value("\${jwt.refresh-duration}")
    var refreshDuration: Long = 2592000
}