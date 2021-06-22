package com.brtrip.trip.domain

import com.brtrip.common.exceptions.NotFoundException
import com.brtrip.place.Place
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class TripFinder(
    private val tripRepository: TripRepository,
    private val stopFinder: StopFinder
) {
    fun findByUserId(userId: Long): List<Trip> {
        return tripRepository.findByUserIdAndDeleted(userId, false)
    }

    fun findRecent(userId: Long): Trip {
        return tripRepository.findFirstByUserIdAndDeletedOrderByCreatedAtDesc(userId, false)
            ?: throw NotFoundException("여행 일정이 없습니다.")
    }

    fun findById(tripId: Long): Trip {
        return tripRepository.findByIdAndDeleted(tripId, false)
            ?: throw NotFoundException("여행 일정이 없습니다.")
    }

    fun findIncludePlace(place: Place): List<Trip> {
        val tripIds = stopFinder.findByPlaceId(place.id!!)
            .map { it.trip.id!! }

        return tripRepository.findAllById(tripIds)
    }
}