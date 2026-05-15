package org.example.practice.repositories

import org.example.practice.model.Ticket
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.http.ResponseEntity

interface TicketRepository : JpaRepository<Ticket, Long> {

    fun create(ticket: Ticket)

    fun getTicketById(ticketId: Long): ResponseEntity<Ticket>


}