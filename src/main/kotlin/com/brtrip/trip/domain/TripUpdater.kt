package com.brtrip.trip.domain

import com.brtrip.path.Path
import com.brtrip.path.domain.*
import com.brtrip.place.Place
import com.brtrip.place.PlaceFinder
import com.brtrip.place.PlaceRepository
import com.brtrip.place.PlaceRequest
import com.brtrip.trip.controller.request.TripRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.stream.IntStream

@Component
@Transactional
class TripUpdater(
    private val tripFinder: TripFinder,
    private val pathFinder: PathFinder,
    private val placeFinder: PlaceFinder,
    private val tripPathRepository: TripPathRepository,
    private val pathPlaceRepository: PathPlaceRepository,
    private val placeRepository: PlaceRepository
) {
    fun update(tripId: Long, request: TripRequest) {
        // 1. trip 수정
        // Path와 관련되지 않은 정보를 우선 일괄 업데이트한다
        val trip = tripFinder.findById(tripId)
        trip.title = request.title
        trip.memo = request.memo

        // 2. path 수정
            // 1) tripId로 List<tripPath>를 가져온다
            // 2) pathId로 List<PathPlace>를 가져온다
            // 3) DB에 저장된 path의 place 목록과 요청으로 들어온 path의 place 목록을 비교한다
                // 3-1) place가 달라졌다면, 달라진 List<Place>를 가지는 Path가 DB에 있는지 조회한다
                    // 3-1-1) 달라진 Path가 DB에 있으면, 이전 Path의 likeCount를 1 감소시킨다
                    // 3-1-2) 달라진 Path가 DB에 없으면, 새로운 Path와 PathPlace를 만든다
                    // 새로운 Path의 likeCount를 1 증가시키고, 기존 Path의 TripPath를 지우고, 새로운 TripPath를 만든다
            // 3-2) place가 동일하다면, 다음 Path로 넘어가 3)부터의 작업을 반복한다

        request.paths.forEach {
            // db
            val path = pathFinder.findById(it.id!!)
            val places = placeFinder.findByPath(path)

            if (pathChanged(it.places, places)) {
                val pathExistChecker = requestPathExist(it.places)
                val newPath: Path
                // 3-1-1
                if (pathExistChecker.getOrDefault(true, -1L) != -1L) {
                    newPath = pathExistChecker.get(true)!!
                    path.likeCount--
                // 3-1-2
                } else {
                    newPath = pathExistChecker.get(false)!!
                    it.places.forEachIndexed { index, placeRequest ->
                        pathPlaceRepository.save(PathPlace(
                            path = newPath,
                            place = placeRepository.findByLatAndLng(placeRequest.lat, placeRequest.lng)!!,
                            sequence = index+1
                        ))
                    }
                }
                newPath.likeCount++
                tripPathRepository.deleteByTripAndPath(trip, path)
                tripPathRepository.save(TripPath(trip = trip, path = newPath))
            }
        }
    }

    // Path가 존재하면 true, 없으면 false를 key로 가지는 Map을 리턴
    private fun requestPathExist(places: List<PlaceRequest>): Map<Boolean, Path> {
        return pathFinder.findByPlacesToCheckPath(places)
    }

    fun pathChanged(placeRequests: List<PlaceRequest>, savedPlaces: List<Place>): Boolean {
        if (placeRequests.size != savedPlaces.size) return true

        var isChanged: Boolean = false
        IntStream.range(0, savedPlaces.size).forEach { i ->
            if (savedPlaces[i].lat != placeRequests[i].lat || savedPlaces[i].lng != placeRequests[i].lng) {
                isChanged = true
            }
        }
        return isChanged
    }
}