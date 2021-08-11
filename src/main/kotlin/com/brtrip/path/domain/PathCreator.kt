package com.brtrip.path.domain

import com.brtrip.path.Path
import com.brtrip.path.controller.request.PathRequest
import com.brtrip.place.Place
import com.brtrip.place.PlaceRepository
import com.brtrip.place.PlaceRequest
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
            placeRepository.save(
                Place(
                    lat = placeRequest.lat,
                    lng = placeRequest.lng,
                    name = placeRequest.name,
                    content = placeRequest.content
                ))
            pathPlaceRepository.save(PathPlace(
                path = path,
                place = placeRepository.findByLatAndLng(placeRequest.lat, placeRequest.lng)!!,
                sequence = index
            ))
        }
        return path
    }

    fun createPathWithinTrip(request: List<PathRequest>, idx: Int): Path {
        val pathRequest = request[idx]
        val path = pathRepository.findById(pathRequest.id!!)

        pathRequest.places.forEachIndexed { index, placeRequest ->
            pathPlaceRepository.save(PathPlace(
                path = path.get(),
                place = placeRepository.findByLatAndLng(placeRequest.lat, placeRequest.lng)!!,
                sequence = index
            ))
        }

        return path.get()
    }

    fun createBy(): Path {
        return pathRepository.save(Path())
    }
}
