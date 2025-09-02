package org.example.practice.exception

import java.time.LocalDateTime

data class ApiError(
    val timestamp: LocalDateTime,
    val status: Int,
    val error: String,
    val message: String,
    val details: Map<String, String>
)
