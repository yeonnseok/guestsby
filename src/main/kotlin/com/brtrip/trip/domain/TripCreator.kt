package com.brtrip.trip.domain

import com.brtrip.path.domain.PathFinder
import com.brtrip.place.PlaceFinder
import com.brtrip.place.Places
import com.brtrip.trip.controller.request.TripRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class TripCreator(
    private val tripRepository: TripRepository,
    private val tripPathRepository: TripPathRepository,
    private val pathFinder: PathFinder,
    private val placeFinder: PlaceFinder
) {
    fun create(userId: Long, request: TripRequest): Trip {
        val trip = tripRepository.save(request.toEntity(userId))

        request.paths.forEachIndexed { index, it ->
            val savedPlaces = placeFinder.findByPath(pathFinder.findById(it.id))

            val newPath = if (Places(savedPlaces).isChanged(it.places)) {
                pathFinder.findOrCreatePathByPlaces(it.places)
            } else {
                pathFinder.findById(it.id)
            }
            newPath.likeCount++
            tripPathRepository.save(TripPath(
                trip = trip,
                path = newPath,
                sequence = index + 1
            ))
        }
        return trip
    }
}
