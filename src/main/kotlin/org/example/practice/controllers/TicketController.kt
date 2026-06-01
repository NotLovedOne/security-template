package org.example.practice.controllers

import org.example.practice.dto.TicketDto
import org.example.practice.model.Ticket
import org.example.practice.services.TicketService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController()
@RequestMapping("/tickets")
class TicketController (
    private val ticketService: TicketService
){
    @PostMapping("/create")
    fun createTicket(@RequestBody ticket: TicketDto) : ResponseEntity<TicketDto> {
        return ticketService.createTicket(ticket)
    }

}