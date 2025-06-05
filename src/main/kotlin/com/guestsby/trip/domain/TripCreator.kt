package com.guestsby.trip.domain

import com.guestsby.path.domain.PathCreator
import com.guestsby.path.domain.PathFinder
import com.guestsby.place.PlaceFinder
import com.guestsby.place.Places
import com.guestsby.trip.controller.request.TripRequest
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
