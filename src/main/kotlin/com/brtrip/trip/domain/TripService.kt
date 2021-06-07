package com.brtrip.trip.domain

import com.brtrip.trip.controller.request.TripCreateRequest
import com.brtrip.trip.controller.response.TripResponse
import org.springframework.stereotype.Service

@Service
class TripService(
    private val tripCreator: TripCreator,
    private val tripFinder: TripFinder
) {
    fun create(userId: Long, request: TripCreateRequest): Long {
        val trip = tripCreator.create(userId, request)
        return trip.id!!
    }

    fun findMyTrips(userId: Long): List<TripResponse> {
        return tripFinder.findByUserId(userId)
            .map { TripResponse.of(it) }
    }

    fun findRecentTrip(userId: Long): TripResponse {
        val trip = tripFinder.findRecent(userId)
        return TripResponse.of(trip)
    }
}
