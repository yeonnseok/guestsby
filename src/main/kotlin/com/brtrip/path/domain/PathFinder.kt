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

    fun findBy(places: List<PlaceRequest>): Map<Boolean, Path> {
        // k: pathId, v: pathId가 k인 path 안의 sequence가 일치하는 횟수 -> sequence가 모두 일치하면 새 place 목록을 가지는 경로가 기존에 존재하는것
        val sequenceCheckMap = mutableMapOf<Long, Int>()
        var pathId: Long = -1

        // per: 요청으로 들어온 placeRequest
        places.forEachIndexed { index, value ->
            val place = placeRepository.findByLatAndLng(value.lat, value.lng)
            val pathPlaces = pathPlaceRepository.findByPlaceId(place?.id!!)

            // per: DB에서 가져온 pathPlace
            pathPlaces.forEach { pathPlace ->
                if (index+1 == pathPlace.sequence) {
                    if (sequenceCheckMap.getOrDefault(pathPlace.path.id, -1) == -1) {
                        sequenceCheckMap.put(pathPlace.path.id!!, 1)
                    } else {
                        sequenceCheckMap.put(pathPlace.path.id!!, sequenceCheckMap.get(pathPlace.path.id)!!+1)
                    }
                }
            }
        }
        sequenceCheckMap.forEach { k, v ->
            if (v == places.size) {
                pathId = k
            }
        }

        if (pathId == -1L) {
            return mapOf(
                false to pathRepository.save(Path()))
        }

        return mapOf(
            true to pathRepository.findById(pathId)
                .orElseThrow { NotFoundException("해당하는 경로가 없습니다.") })
    }
}