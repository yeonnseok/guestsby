package com.brtrip.trip.domain

import com.brtrip.common.exceptions.NotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class TripFinder(
    private val tripRepository: TripRepository
) {
    fun findByUserId(userId: Long): List<Trip> {
        return tripRepository.findByUserId(userId)
    }

    fun findRecent(userId: Long): Trip {
        return tripRepository.findFirstByUserIdOrderByCreatedAtDesc(userId)
            ?: throw NotFoundException("여행 일정이 없습니다.")
    }

    fun findById(tripId: Long): Trip {
        return tripRepository.findById(tripId)
            .orElseThrow { NotFoundException("여행 일정이 없습니다.") }
    }
}