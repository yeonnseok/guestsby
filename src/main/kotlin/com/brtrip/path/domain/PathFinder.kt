package com.brtrip.path.domain

import com.brtrip.common.exceptions.NotFoundException
import com.brtrip.path.Path
import com.brtrip.place.PlaceRepository
import com.brtrip.place.PlaceRequest
import com.brtrip.trip.domain.TripPathRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class PathFinder(
    private val pathRepository: PathRepository,
    private val tripPathRepository: TripPathRepository,
    private val pathPlaceRepository: PathPlaceRepository,
    private val placeRepository: PlaceRepository
) {
    fun findByPlaceId(placeId: Long): List<Path> {
        val pathPlaces = pathPlaceRepository.findByPlaceId(placeId)

        return pathPlaces.map {
            pathRepository.findById(it.path.id!!)
                .orElseThrow { NotFoundException("해당 장소가 포함된 경로가 존재하지 않습니다.") }
        }
    }

    fun findById(id: Long): Path {
        return pathRepository.findById(id)
            .orElseThrow { NotFoundException("해당 경로가 존재하지 않습니다.") }
    }

    fun findBy(places: List<PlaceRequest>): Path {
        val pathIdMap = mutableMapOf<Long, Int>()
        var pathId: Long = 0

        places.forEach {
            val place = placeRepository.findByLatAndLng(it.lat, it.lng)
            val pathPlaces = pathPlaceRepository.findByPlaceId(place?.id!!)

            pathPlaces.forEach { pathPlace ->
                if (pathIdMap.getOrDefault(pathPlace.path.id, -1) == -1) {
                    pathIdMap.put(pathPlace.path.id!!, 1)
                } else {
                    val prevVal = pathIdMap.get(pathPlace.path.id)
                    if (prevVal != null) {
                        pathIdMap.put(pathPlace.path.id!!, prevVal+1)
                    }
                }
            }
        }

        pathIdMap.forEach { k, v ->
            if (v == places.size) {
                pathId = k
            }
        }

        return pathRepository.findById(pathId).orElseThrow { NotFoundException("해당 경로가 존재하지 않습니다.") }
    }
}