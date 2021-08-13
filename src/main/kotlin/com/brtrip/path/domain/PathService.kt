package com.brtrip.path.domain

import com.brtrip.path.controller.request.PathRequest
import com.brtrip.path.controller.response.PathResponse
import com.brtrip.place.PlaceFinder
import com.brtrip.place.dto.PlaceResponse
import org.springframework.stereotype.Service

@Service
class PathService(
    private val pathCreator: PathCreator,
    private val pathFinder: PathFinder,
    private val placeFinder: PlaceFinder
) {
    fun create(request: PathRequest): Long {
        val path = pathCreator.create(request)
        return path.id!!
    }

    fun recommendPaths(lat: String, lng: String): List<PathResponse> {
        val place = placeFinder.findByPosition(lat, lng)
        val paths = pathFinder.findByPlacesToCheckPath(place)
        return paths.map {
            val places = placeFinder.findByPath(it).map { place -> PlaceResponse.of(place) }
            PathResponse(places, it.likeCount)
        }
    }
}
