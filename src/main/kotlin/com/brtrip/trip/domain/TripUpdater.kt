package com.brtrip.trip.domain

import com.brtrip.path.domain.*
import com.brtrip.place.Place
import com.brtrip.place.PlaceFinder
import com.brtrip.place.PlaceRepository
import com.brtrip.place.PlaceRequest
import com.brtrip.trip.controller.request.TripRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class TripUpdater(
    private val tripFinder: TripFinder,
    private val pathFinder: PathFinder,
    private val placeFinder: PlaceFinder,
    private val tripPathRepository: TripPathRepository,
    private val pathPlaceRepository: PathPlaceRepository,
    private val placeRepository: PlaceRepository,
    private val pathRepository: PathRepository,
    private val pathCreator: PathCreator
) {
    fun update(tripId: Long, tripRequest: TripRequest): Trip {
        val trip = tripFinder.findById(tripId)
        trip.title = tripRequest.title
        trip.memo = tripRequest.memo

        var changedFlag = false
        val tripPaths = tripRequest.paths.mapIndexed { index, it ->
            val priorPath = pathFinder.findById(it.id!!)
            val priorPlaces = placeFinder.findByPath(priorPath)
            val newPath = if (isPathChanged(it.places, priorPlaces)) {
                changedFlag = true
                pathCreator.create(it.places)
            } else priorPath

            newPath.likeCount++
            priorPath.likeCount--
            TripPath(trip = trip, path = newPath, sequence = index + 1)
        }

        if (changedFlag) {
            trip.tripPaths.forEach { it.delete() }
            trip.tripPaths = tripPaths.toMutableList()
        }
        return trip
    }

    fun isPathChanged(placeRequests: List<PlaceRequest>, savedPlaces: List<Place>): Boolean {
        if (placeRequests.size != savedPlaces.size) return true
        savedPlaces.forEachIndexed { index, place ->
            if (place.lat != placeRequests[index].lat || place.lng != placeRequests[index].lng) return true
        }
        return false
    }
}
