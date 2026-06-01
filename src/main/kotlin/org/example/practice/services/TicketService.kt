package org.example.practice.services

import org.example.practice.dto.TicketDto
import org.example.practice.model.Ticket
import org.example.practice.model.TicketStatus
import org.example.practice.repositories.TicketRepository
import org.example.practice.repositories.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronization
import org.springframework.transaction.support.TransactionSynchronizationManager

@Service
class TicketService(
    private val redisService: RedisService,
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val ticketRepository: TicketRepository,
    private val userRepository: UserRepository
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun createTicket(dto: TicketDto): ResponseEntity<TicketDto> {
        val reporterUsername = SecurityContextHolder.getContext().authentication.name
        val reporter = userRepository.findByUsername(reporterUsername)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        val assignees = dto.assignees
            .mapNotNull { userRepository.findByUsername(it) }
            .toMutableSet()

        val status = dto.status
            ?.let { runCatching { TicketStatus.valueOf(it.uppercase()) }.getOrDefault(TicketStatus.OPEN) }
            ?: TicketStatus.OPEN

        val ticket = Ticket(
            title = dto.title,
            description = dto.description,
            status = status,
            reporter = reporter,
            assignees = assignees
        )

        return try {
            val saved = ticketRepository.save(ticket)

            TransactionSynchronizationManager.registerSynchronization(
                object : TransactionSynchronization {
                    override fun afterCommit() {
                        kafkaTemplate.send("ticket-created", saved.id.toString())
                        redisService.set("ticket:${saved.id}", saved.id.toString())
                    }
                }
            )
//            kafkaTemplate.send("ticket-created", saved.id.toString())
//            redisService.set("ticket:${saved.id}", saved.id.toString())
            val response = dto.copy(
                reporter = reporterUsername,
                status = saved.status.name,
                createdAt = saved.createdAt.toString()
            )
            ResponseEntity(response, HttpStatus.CREATED)
        } catch (ex: Exception) {
            logger.error("Error while creating ticket: {}", ex.message)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }
}
