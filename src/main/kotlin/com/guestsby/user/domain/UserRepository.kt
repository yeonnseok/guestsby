package com.guestsby.user.domain

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(email: String): User?

    fun findByIdAndDeleted(userId: Long, deleted: Boolean): User?

    fun existsByEmail(email: String): Boolean
}
