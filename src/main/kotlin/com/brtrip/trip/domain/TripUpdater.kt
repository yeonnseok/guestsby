package com.brtrip.trip.domain

import com.brtrip.common.exceptions.NotFoundException
import com.brtrip.path.domain.PathFinder
import com.brtrip.path.domain.PathPlace
import com.brtrip.path.domain.PathPlaceFinder
import com.brtrip.place.PlaceFinder
import com.brtrip.trip.controller.request.TripRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import kotlin.io.path.Path

@Component
@Transactional
class TripUpdater(
    private val tripFinder: TripFinder,
    private val pathFinder: PathFinder,
    private val placeFinder: PlaceFinder,
    private val pathPlaceFinder: PathPlaceFinder
) {
    fun update(tripId: Long, request: TripRequest) {
        // 1. trip 수정
        // Path와 관련되지 않은 정보를 우선 업데이트한다
        val trip = tripFinder.findById(tripId)
        trip.title = request.title
        trip.memo = request.memo

//        val tripPaths = pathFinder.findByTripId(tripId)
//        tripPaths.forEach { tp ->
//            val pathPlaces = pathPlaceFinder.findByPathId(tp.path.id!!)
//            request.paths.mapIndexed { index, value ->
//                PathPlace(
//                    path = tp.path,
//                    place = value.places[index],
//                    sequence = index.toLong()
//                )
//            }
//        }

//        // 2. path 수정
//        // 1) tripId로 List<tripPath>를 가져온다
//        // 2) pathId로 List<PathPlace>를 가져온다
//        // 3) DB에 저장된 path의 place 목록과 요청으로 들어온 path의 place 목록을 비교한다
//          // 3-1) place가 달라졌다면, 달라진 List<Place>를 가지는 Path가 DB에 있는지 조회한다
//              // 3-1-1) 달라진 Path가 DB에 있으면, likeCount를 1 증가시키고 PathPlace, TripPlace를 업데이트한다
//                  // 이전 Path는 likeCount를 1 감소시키고, likeCount가 0이라면 삭제한다.
//              // 3-1-2) 달라진 Path가 DB에 없으면, 새로 Path를 저장하고, PathPlace, TripPlace를 업데이트한다
//          // 3-2) place가 동일하다면, 다음 Path로 넘어가 3)부터의 작업을 반복한다

//        val tripPaths = pathFinder.findByTripId(tripId)
//        tripPaths.forEach { tp ->
//            // DB에서 pathId로 pathPlace를 불러 와야 할 듯
//            val places = placeFinder.findByPathId(tp.path.id!!)
//            val pathPlaces = pathPlaceFinder.findByPathId(tp.path.id!!)
//            request.paths.forEach { pr ->
//                // pathId가 같은데 place가 다른 경우 (수정된 경우)
//                if (pr.id == tp.path.id && pr.places != places) {
//                    // 바뀐 place를 가지는 path가 db에 있는지 조회해야함
//
//                    pathPlaces = PathPlace(
//                            path = tp.path,
//                            place = it,
//                            sequence = pr.places.indexOf(it)
//                        )
//                }
//            }
//        }
    }
}
