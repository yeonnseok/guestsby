package com.brtrip.trip.domain

import com.brtrip.common.exceptions.NotFoundException
import com.brtrip.path.Path
import com.brtrip.path.domain.PathCreator
import com.brtrip.path.domain.PathFinder
import com.brtrip.path.domain.PathRepository
import com.brtrip.place.PlaceFinder
import com.brtrip.trip.controller.request.TripRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class TripCreator(
    private val tripRepository: TripRepository,
    private val tripPathRepository: TripPathRepository,
    private val pathCreator: PathCreator,
    private val pathRepository: PathRepository,
    private val pathFinder: PathFinder,
    private val tripUpdater: TripUpdater,
    private val placeFinder: PlaceFinder
) {
    fun create(userId: Long, request: TripRequest): Trip {
        // 1. trip 저장
        val trip = tripRepository.save(request.toEntity(userId))

        // 2. tripPath 저장
        request.paths.forEach {
            // pathId를 통해 path가 바뀌었는지 조회
            val savedPlaces = placeFinder.findByPath(pathFinder.findById(it.id!!))
            val newPath: Path
            if (tripUpdater.pathChanged(it.places, savedPlaces)) {
                // path가 바뀌었으면, 해당 places들을 가지는 path가 있는지 다시 조회
                val pathExistId = pathFinder.findByPlaces(it.places)
                if (pathExistId != -1L) {
                    val pathExist = pathRepository.findById(pathExistId!!)
                        .orElseThrow { NotFoundException("경로를 찾을 수 없습니다.") }
                    pathExist.likeCount++
                    newPath = pathExist
                } else {
                    newPath = pathCreator.create(it)
                }
            } else {
                // path가 안 바뀌었으면 요청 안의 pathId로 path 들고 옴
                newPath = pathFinder.findById(it.id!!)
            }
            tripPathRepository.save(TripPath(
                trip = trip,
                path = newPath
            ))
        }
        return trip
    }
}
