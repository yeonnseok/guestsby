package com.brtrip.path.domain

import com.brtrip.common.exceptions.NotFoundException
import com.brtrip.path.Path
import com.brtrip.place.Place
import com.brtrip.place.PlaceRepository
import com.brtrip.place.PlaceRequest
import com.brtrip.trip.domain.Trip
import com.brtrip.trip.domain.TripPath
import com.brtrip.trip.domain.TripPathRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class PathFinder(
    private val tripPathRepository: TripPathRepository,
    private val pathRepository: PathRepository,
    private val pathPlaceRepository: PathPlaceRepository,
    private val placeRepository: PlaceRepository
) {
    fun findByPlacesToCheckPath(place: Place): List<Path> {
        val pathPlaces = pathPlaceRepository.findByPlace(place)

        return pathPlaces.map {
            pathRepository.findById(it.path.id!!)
                .orElseThrow { NotFoundException("해당 장소가 포함된 경로가 존재하지 않습니다.") }
        }
    }

    fun findById(id: Long): Path {
        return pathRepository.findById(id)
            .orElseThrow { NotFoundException("해당 경로가 존재하지 않습니다.") }
    }

    fun findOrCreatePathByPlaces(places: List<PlaceRequest>): Path {
        val firstPlace = placeRepository.findByLatAndLng(places[0].lat, places[0].lng) ?: throw NotFoundException("존재하지 않는 장소 입니다.")
        val firstPathPlaces = pathPlaceRepository.findByPlaceAndSequence(firstPlace, 1)

        val pathPlacesList = firstPathPlaces.map { firstPathPlace ->
            places.mapIndexedNotNull { index, placeRequest ->
                val place = placeRepository.findByLatAndLng(placeRequest.lat, placeRequest.lng)
                    ?: throw NotFoundException("존재하지 않는 장소 입니다.")
                pathPlaceRepository.findByPathAndPlaceAndSequence(firstPathPlace.path, place, index + 1)
            }
        }.filter { it.size == places.size }

        if (pathPlacesList.isNotEmpty()) {
            return pathPlacesList.first().first().path
        }

        val path = Path()
        places.mapIndexed { index, place ->
            val findPlace = placeRepository.findByLatAndLng(place.lat, place.lng) ?: throw NotFoundException("존재하지 않는 장소 입니다.")
            pathPlaceRepository.save(PathPlace(path = path, place = findPlace, sequence = index + 1))
        }
        return path

//        /*
//            k: pathId,
//            v: pathId가 k인 path 안의 sequence가 일치하는 횟수
//                -> sequence가 모두 일치하면 새 place 목록을 가지는 경로가 기존에 존재하는 것으로 판단
//         */
//        val sequenceCheckMap = mutableMapOf<Long, Int>()
//        var pathId: Long = -1L
//
//        // per: 요청으로 들어온 placeRequest
//        places.forEachIndexed { index, placeRequest ->
//            val place = placeRepository.findByLatAndLng(placeRequest.lat, placeRequest.lng)
//            val pathPlaces = pathPlaceRepository.findByPlace(place!!)
//
//            // per: DB에서 가져온 pathPlace
//            pathPlaces.forEach { pathPlace ->
//                if (index+1 == pathPlace.sequence) {
//                    if (sequenceCheckMap.getOrDefault(pathPlace.path.id, -1) == -1) {
//                        sequenceCheckMap.put(pathPlace.path.id!!, 1)
//                    } else {
//                        sequenceCheckMap.put(pathPlace.path.id!!, sequenceCheckMap.get(pathPlace.path.id)!!+1)
//                    }
//                }
//            }
//        }
//        sequenceCheckMap.forEach { k, v ->
//            if (v == places.size) {
//                pathId = k
//            }
//        }
//        return pathId
    }

    private fun isNewPath(
        pathPlaces: List<PathPlace>,
        places: List<PlaceRequest>
    ): Boolean {
        if (pathPlaces.size != places.size) return true
        if (pathPlaces.map { it.path.id }.toHashSet().size != 1) return true
        return false
    }

    fun findByTrip(trip: Trip): List<Path> {
        return tripPathRepository.findByTrip(trip)
            .map { it.path }
    }
}
