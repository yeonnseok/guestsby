package com.guestsby.path.domain

import com.guestsby.path.controller.request.PathRequest
import com.guestsby.path.controller.response.PathResponse
import com.guestsby.place.PlaceFinder
import org.springframework.stereotype.Service

@Service
class PathService(
    private val pathCreator: PathCreator,
    private val placeFinder: PlaceFinder,
) {
    fun create(request: PathRequest): Long {
        val path = pathCreator.create(request.places)
        return path.id!!
    }
}
