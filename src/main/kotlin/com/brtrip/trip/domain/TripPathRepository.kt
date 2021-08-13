package com.brtrip.trip.domain

import com.brtrip.path.Path
import org.springframework.data.jpa.repository.JpaRepository

interface TripPathRepository : JpaRepository<TripPath, Long> {
    fun deleteByTripAndPath(trip: Trip, path: Path)

    fun findByTrip(trip: Trip): List<TripPath>
}
