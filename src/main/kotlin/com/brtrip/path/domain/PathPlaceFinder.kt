package com.brtrip.path.domain

import com.brtrip.common.exceptions.NotFoundException
import com.brtrip.place.Place
import com.brtrip.trip.domain.TripRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class PathPlaceFinder(
    private val pathPlaceRepository: PathPlaceRepository
) {
    fun findByPathId(pathId: Long): List<PathPlace> {
        return pathPlaceRepository.findByPathId(pathId)
//        return pathPlaces.map {
//            placeRepository.findById(it.path.id!!)
//                .orElseThrow { NotFoundException("경로 내에 포함된 장소를 찾을 수 없습니다.") }
//        }
    }
}