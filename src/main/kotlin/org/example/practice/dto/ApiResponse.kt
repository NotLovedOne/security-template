package org.example.practice.dto

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class ApiResponse<T>(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: T? = null
) {
    companion object {
        fun <T> success(data: T, message: String = "Operation successful", status: Int = HttpStatus.OK.value()): ApiResponse<T> {
            return ApiResponse(
                status = status,
                success = true,
                message = message,
                data = data
            )
        }

        fun <T> error(message: String, status: Int = HttpStatus.BAD_REQUEST.value()): ApiResponse<T> {
            return ApiResponse(
                status = status,
                success = false,
                message = message,
                data = null
            )
        }
    }
}
