package org.example.practice.controllers

import org.example.practice.dto.UserResponse
import org.example.practice.utils.CustomUserDetails
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController {

    private val log = LoggerFactory.getLogger(UserController::class.java)

    @GetMapping("/me")
    fun me(@AuthenticationPrincipal userDetails: CustomUserDetails): ResponseEntity<UserResponse> {
        log.info("User info requested for: {}", userDetails.username)
        val user = userDetails.getUser()
        return ResponseEntity.ok(UserResponse(id = user.id, username = user.username, email = user.email))
    }
}