package com.brtrip.path.domain

import com.brtrip.path.controller.request.PathRequest
import com.brtrip.place.PlaceRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class PathCreator(
    private val pathRepository: PathRepository,
    private val pathPlaceRepository: PathPlaceRepository,
    private val placeRepository: PlaceRepository
) {
    fun create(request: PathRequest): Path {
        val path = pathRepository.save(request.toEntity())

        request.places.forEachIndexed { index, placeRequest ->
            pathPlaceRepository.save(PathPlace(
                path = path,
                place = placeRepository.findByLatAndLng(placeRequest.lat, placeRequest.lng)!!,
                sequence = index+1
            ))
        }
        return path
    }
}
