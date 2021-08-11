package com.brtrip.trip.domain

import com.brtrip.common.exceptions.NotFoundException
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
import kotlin.io.path.Path

@Component
@Transactional
class TripUpdater(
    private val tripFinder: TripFinder,
    private val pathFinder: PathFinder,
    private val placeFinder: PlaceFinder,
    private val tripPathRepository: TripPathRepository,
    private val pathPlaceRepository: PathPlaceRepository,
    private val placeRepository: PlaceRepository,
    private val pathRepository: PathRepository,
    private val pathCreator: PathCreator
) {
    fun update(tripId: Long, request: TripRequest) {
        // 1. trip 수정
        // Path와 관련되지 않은 정보를 우선 업데이트한다
        val trip = tripFinder.findById(tripId)
        trip.title = request.title
        trip.memo = request.memo

        // 2. path 수정
        // 1) tripId로 List<tripPath>를 가져온다
        // 2) pathId로 List<PathPlace>를 가져온다
        // 3) DB에 저장된 path의 place 목록과 요청으로 들어온 path의 place 목록을 비교한다
            // 3-1) place가 달라졌다면, 달라진 List<Place>를 가지는 Path가 DB에 있는지 조회한다
                // 3-1-1) 달라진 Path가 DB에 있으면, likeCount를 1 증가시키고, 기존 TripPath를 지우고 새 TripPath를 생성
                    // 이전 Path는 likeCount를 1 감소시킨다
                // 3-1-2) 달라진 Path가 DB에 없으면, 새로 Path를 저장하고, 새 PathPlace를 만들고 TripPath를 추가한다
        // 3-2) place가 동일하다면, 다음 Path로 넘어가 3)부터의 작업을 반복한다

        request.paths.forEach {
            // db
            val path = pathFinder.findById(it.id!!)
            val places = placeFinder.findByPathId(path.id!!)

            if (pathChanged(it.places, places)) {
                val newPath = requestPathExist(it.places)
                // 3-1-1
                if (newPath.id != -1L) {
                    tripPathRepository.deleteByTripAndPath(trip, path)
                    val prevPath = pathFinder.findBy(it.places)
                    prevPath.likeCount--
                // 3-1-2
                } else {
                    it.places.forEachIndexed { index, placeRequest ->
                        pathPlaceRepository.save(PathPlace(
                            path = pathCreator.createBy(),
                            place = placeRepository.findByLatAndLng(placeRequest.lat, placeRequest.lng)!!,
                            sequence = index
                        ))
                    }
                }
                newPath.likeCount++
                tripPathRepository.save(TripPath(trip = trip, path = newPath))
            }
        }
    }

    private fun requestPathExist(places: List<PlaceRequest>): Path {
        val path = pathFinder.findBy(places)
        if (path != null) {
            return path
        }
        path.id = -1L
        return path
    }

    private fun pathChanged(placeRequests: List<PlaceRequest>, places: List<Place>): Boolean {
        if (placeRequests.size != places.size) return true

        var isChanged: Boolean = false
        IntStream.range(0, places.size).forEach { i ->
            if (places[i].lat != placeRequests[i].lat || places[i].lng != placeRequests[i].lng) {
                isChanged = true
            }
        }
        return isChanged
    }
}