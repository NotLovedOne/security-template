package org.example.practice.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "tickets")
class Ticket(

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long = 0,

    @Column(nullable = false)
    var title: String,
    @Column(nullable = false)
    var description: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: TicketStatus,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var reporter: User,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "ticket_assignee",
        joinColumns = [JoinColumn(name = "ticket_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    var assignees: MutableSet<User> = mutableSetOf(),

    @Column(nullable = false, updatable = false)
    val createdAt: Instant = Instant.now(),

    @Column(nullable = true,updatable = true)
    var updatedAt: Instant = Instant.now(),
    )


enum class TicketStatus {
    OPEN, IN_PROGRESS, RESOLVED, CLOSED
}

