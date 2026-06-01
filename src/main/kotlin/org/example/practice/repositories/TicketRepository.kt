package org.example.practice.repositories

import org.example.practice.model.Ticket
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface TicketRepository : JpaRepository<Ticket, UUID>