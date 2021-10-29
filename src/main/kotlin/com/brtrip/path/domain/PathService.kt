package com.brtrip.path.domain

import com.brtrip.path.controller.request.PathRequest
import com.brtrip.path.controller.response.PathResponse
import com.brtrip.recommend.Recommendation
import com.brtrip.place.PlaceFinder
import org.springframework.stereotype.Service

@Service
class PathService(
    private val pathCreator: PathCreator,
    private val placeFinder: PlaceFinder
) {
    fun create(request: PathRequest): Long {
        val path = pathCreator.create(request.places)
        return path.id!!
    }

    fun recommend(lat: String, lng: String): List<PathResponse> {
        val place = placeFinder.findByPosition(lat, lng)
        return Recommendation().run(place)
    }
}
