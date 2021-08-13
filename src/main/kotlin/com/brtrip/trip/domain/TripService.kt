package com.brtrip.trip.domain

import com.brtrip.common.exceptions.AuthorizationException
import com.brtrip.path.domain.PathFinder
import com.brtrip.trip.controller.request.TripRequest
import com.brtrip.trip.controller.response.TripResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.logging.Logger.getLogger

@Service
class TripService(
    private val tripCreator: TripCreator,
    private val tripFinder: TripFinder,
    private val tripUpdater: TripUpdater,
    private val tripDeleter: TripDeleter,
    private val pathFinder: PathFinder
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun create(userId: Long, request: TripRequest): Long {
        val trip = tripCreator.create(userId, request)
        return trip.id!!
    }

    fun findMyTrips(userId: Long): List<TripResponse> {
        val trips = tripFinder.findByUserId(userId)
        log.info("trip paths - {}", trips[0].tripPaths)
        return trips.map { TripResponse.of(it) }
    }

    fun update(userId: Long, tripId: Long, request: TripRequest): TripResponse {
        validateAuthorization(userId, tripId)
        val trip = tripFinder.findById(tripId)
        val tripPath = trip.tripPaths
        log.info("tripPath111 - {}", tripPath)
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

    fun deletePathInTrip(userId: Long, tripId: Long, pathId: Long) {
        validateAuthorization(userId, tripId)
        tripDeleter.deletePathInTrip(tripId, pathId)
    }
}
