package com.guestsby.path.domain

import org.springframework.data.jpa.repository.JpaRepository

interface PathRepository : JpaRepository<Path, Long> {
    fun findByIdAndDeleted(id: Long, deleted: Boolean): Path?

}
