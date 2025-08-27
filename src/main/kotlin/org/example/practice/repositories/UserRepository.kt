package org.example.practice.repositories

import org.example.practice.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
    fun findByEmail(email: String): User?
    fun findByUsernameAndPassword(username: String, password: String): User?
    fun findByEmailAndPassword(email: String, password: String): User?
}