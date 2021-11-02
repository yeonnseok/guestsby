package com.brtrip.trip.domain

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class TripDeleter(
    private val tripFinder: TripFinder
) {
    fun delete(tripId: Long) {
        val trip = tripFinder.findById(tripId)
        trip.delete()
        trip.tripPaths.forEach { it.delete() }
    }
}
