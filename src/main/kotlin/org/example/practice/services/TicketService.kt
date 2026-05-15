package org.example.practice.services

import org.example.practice.model.Ticket
import org.example.practice.repositories.TicketRepository
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service


@Service
class TicketService(
    private val redisService: RedisService,
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val ticketRepository: TicketRepository
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun createTicket(ticket: Ticket) : ResponseEntity<Ticket> {
        logger.info("Creating ticket {}", ticket)
        try {
            ticketRepository.save(ticket)
            return ResponseEntity(ticket, HttpStatus.CREATED)
        }
        catch(ex: Exception) {
            logger.error("Error while creating ticket", ex.message)
            return ResponseEntity(ticket, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}