package com.brtrip.trip.domain

import com.brtrip.path.Path
import org.springframework.data.jpa.repository.JpaRepository

interface TripPathRepository : JpaRepository<TripPath, Long> {
    fun findByTripId(pathId: Long): List<TripPath>
    fun deleteByTripAndPath(trip: Trip, path: Path)
}