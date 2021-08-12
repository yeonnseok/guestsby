package com.brtrip.trip.domain

import com.brtrip.path.controller.request.PathRequest
import com.brtrip.path.domain.PathRepository
import com.brtrip.trip.controller.request.TripRequest
import javassist.NotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class TripDeleter(
    private val tripFinder: TripFinder,
    private val tripPathRepository: TripPathRepository,
    private val pathRepository: PathRepository
) {
    fun delete(tripId: Long) {
        val trip = tripFinder.findById(tripId)
        trip.delete()
    }

    fun deletePathInTrip(tripId: Long, pathId: Long) {
        val trip = tripFinder.findById(tripId)
        val path = pathRepository.findById(pathId).orElseThrow { NotFoundException("해당 경로를 찾을 수 없습니다.") }
        path.likeCount--
        
        tripPathRepository.deleteByTripAndPath(trip, path)
    }
}
