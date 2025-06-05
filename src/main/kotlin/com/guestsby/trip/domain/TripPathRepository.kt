package com.guestsby.trip.domain

import org.springframework.data.jpa.repository.JpaRepository

interface TripPathRepository : JpaRepository<TripPath, Long> {
    fun findByTripAndDeleted(trip: Trip, deleted: Boolean): List<TripPath>
}
