package com.guestsby.trip.domain

import org.springframework.data.jpa.repository.JpaRepository

interface TripRepository : JpaRepository<Trip, Long> {
    fun findByUserIdAndDeleted(userId: Long, deleted: Boolean): List<Trip>

    fun findByIdAndDeleted(tripId: Long, deleted: Boolean): Trip?
}
