package com.brtrip.trip.domain

import org.springframework.data.jpa.repository.JpaRepository

interface TripPathRepository : JpaRepository<TripPath, Long> {
    fun findByTripId(pathId: Long): List<TripPath>
}