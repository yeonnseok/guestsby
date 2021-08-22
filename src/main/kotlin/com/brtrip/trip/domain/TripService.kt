package com.brtrip.trip.domain

import com.brtrip.common.exceptions.AuthorizationException
import com.brtrip.trip.controller.request.TripRequest
import com.brtrip.trip.controller.response.TripResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class TripService(
    private val tripCreator: TripCreator,
    private val tripFinder: TripFinder,
    private val tripUpdater: TripUpdater,
    private val tripDeleter: TripDeleter,
) {
    fun create(userId: Long, request: TripRequest): Long {
        val trip = tripCreator.create(userId, request)
        return trip.id!!
    }

    fun findMyTrips(userId: Long): List<TripResponse> {
        val trips = tripFinder.findByUserId(userId)
        return trips.map { TripResponse.of(it) }
    }

    fun update(userId: Long, tripId: Long, request: TripRequest): TripResponse {
        validateAuthorization(userId, tripId)
        return TripResponse.of(tripUpdater.update(tripId, request))
    }

    private fun validateAuthorization(userId: Long, tripId: Long) {
        val trip = tripFinder.findById(tripId)
        if (trip.userId != userId) {
            throw AuthorizationException("수정 권한이 없습니다.")
        }
    }

    fun delete(userId: Long, tripId: Long) {
        validateAuthorization(userId, tripId)
        tripDeleter.delete(tripId)
    }
}
