package org.example.practice.exception

import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException, request: WebRequest): ResponseEntity<ApiError> {
        val errors = ex.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "Invalid value") }
        log.error("Validation error: {}", errors)

        val apiError = ApiError(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Validation Error",
            message = "Invalid input data",
            details = errors
        )
        return ResponseEntity(apiError, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(ex: ConstraintViolationException, request: WebRequest): ResponseEntity<ApiError> {
        val errors = ex.constraintViolations.associate {
            it.propertyPath.toString() to it.message
        }
        log.error("Constraint violation: {}", errors)

        val apiError = ApiError(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Validation Error",
            message = "Invalid input data",
            details = errors
        )
        return ResponseEntity(apiError, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException, request: WebRequest): ResponseEntity<ApiError> {
        log.error("Bad request: {}", ex.message)

        val apiError = ApiError(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Bad Request",
            message = ex.message ?: "Invalid request parameters",
            details = emptyMap()
        )
        return ResponseEntity(apiError, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentialsException(ex: BadCredentialsException, request: WebRequest): ResponseEntity<ApiError> {
        log.error("Authentication failed: {}", ex.message)

        val apiError = ApiError(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.UNAUTHORIZED.value(),
            error = "Unauthorized",
            message = "Invalid username or password",
            details = emptyMap()
        )
        return ResponseEntity(apiError, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(ex: AccessDeniedException, request: WebRequest): ResponseEntity<ApiError> {
        log.error("Access denied: {}", ex.message)

        val apiError = ApiError(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.FORBIDDEN.value(),
            error = "Forbidden",
            message = "You don't have permission to access this resource",
            details = emptyMap()
        )
        return ResponseEntity(apiError, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(ex: Exception, request: WebRequest): ResponseEntity<ApiError> {
        log.error("Unhandled exception", ex)

        val apiError = ApiError(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = "Internal Server Error",
            message = "An unexpected error occurred",
            details = emptyMap()
        )
        return ResponseEntity(apiError, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
