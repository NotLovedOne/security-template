package org.example.practice.config

import org.example.practice.utils.JwtFilter
import org.example.practice.utils.JwtUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig (private val jwtFilter: JwtFilter) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf{it.disable()}
            .cors { it.disable() }
            .sessionManagement{ SessionCreationPolicy.STATELESS}
            .authorizeHttpRequests{
                auth ->
                auth.requestMatchers("/auth/**").permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)

        http.exceptionHandling {
            it.authenticationEntryPoint { _, resp, _ ->
                resp.status = 401
                resp.contentType = "application/json"
                resp.writer.write("""{"error":"Unauthorized"}""")
            }
            it.accessDeniedHandler { _, resp, _ ->
                resp.status = 403
                resp.contentType = "application/json"
                resp.writer.write("""{"error":"Forbidden"}""")
            }
        }
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()


    @Bean
    fun authenticationManager(config : AuthenticationConfiguration): AuthenticationManager = config.authenticationManager

}