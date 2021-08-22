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
        val path = pathCreator.create(request.places)
        return path.id!!
    }

    // TODO : 추천 고도화
    fun recommend(lat: String, lng: String): List<PathResponse> {
        val place = placeFinder.findByPosition(lat, lng)
        return pathFinder.findByPlace(place)
            .map { PathResponse.of(it, placeFinder.findByPath(it)) }
    }
}
