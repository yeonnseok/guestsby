package com.brtrip.trip.domain

import com.brtrip.path.domain.PathCreator
import com.brtrip.trip.controller.request.TripRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class TripCreator(
    private val tripRepository: TripRepository,
    private val pathCreator: PathCreator,
) {
    fun create(userId: Long, tripRequest: TripRequest): Trip {
        val trip = tripRequest.toEntity(userId).apply {
            tripPaths = tripRequest.paths.mapIndexed { index, pathRequest ->
                val path = pathCreator.create(pathRequest.places)
                path.likeCount++
                TripPath(trip = this, path = path, sequence = index + 1)
            }.toMutableList()
        }
        return tripRepository.save(trip)
    }
}
