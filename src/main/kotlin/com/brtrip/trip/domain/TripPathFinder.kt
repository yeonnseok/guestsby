package com.brtrip.trip.domain

import com.brtrip.path.Path
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class TripPathFinder(
    private val tripPathRepository: TripPathRepository
) {
    fun findBy(trip: Trip): List<TripPath> {
        return tripPathRepository.findByTrip(trip)
    }
}