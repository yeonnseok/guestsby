package com.brtrip.path.domain

import com.brtrip.common.exceptions.NotFoundException
import com.brtrip.path.Path
import com.brtrip.trip.domain.TripPath
import com.brtrip.trip.domain.TripPathRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class PathFinder(
    private val pathRepository: PathRepository,
    private val tripPathRepository: TripPathRepository,
    private val pathPlaceRepository: PathPlaceRepository
) {
    fun findByPlaceId(placeId: Long): List<Path> {
        val pathPlaces = pathPlaceRepository.findByPlaceId(placeId)

        return pathPlaces.map {
            pathRepository.findById(it.path.id!!)
                .orElseThrow { NotFoundException("해당 장소가 포함된 경로가 존재하지 않습니다.") }
        }
    }

    fun findByTripId(tripId: Long): List<TripPath> {
        return tripPathRepository.findByTripId(tripId)
    }
}