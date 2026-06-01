package org.example.practice.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class TicketDto(
    @field:NotBlank(message = "Title is required")
    @field:Size(min = 1, max = 255)
    val title: String,

    val description: String,
    val assignees: List<String> = emptyList(),
    val reporter: String? = null,
    val status: String? = null,
    val createdAt: String? = null
)