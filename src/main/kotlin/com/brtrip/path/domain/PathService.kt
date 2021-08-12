package com.brtrip.path.domain

import com.brtrip.path.controller.request.PathRequest
import com.brtrip.path.controller.response.PathResponse
import com.brtrip.place.PlaceFinder
import org.springframework.stereotype.Service

@Service
class PathService(
    private val pathCreator: PathCreator,
    private val pathFinder: PathFinder,
    private val placeFinder: PlaceFinder,
) {
    fun create(request: PathRequest): Long {
        val path = pathCreator.create(request)
        return path.id!!
    }

    fun recommendPaths(lat: String, lng: String): List<PathResponse> {
        // (위도, 경도) -> Place
        val place = placeFinder.findByPosition(lat, lng)
        // Place -> List<Path>
        val paths = pathFinder.findByPlacesToCheckPath(place)
        // List<Path> -> List<PathResponse>
        return paths.map {
            var places = placeFinder.findByPath(it)
            PathResponse(places, it.likeCount)
        }
    }
}