package com.brtrip.trip.domain

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class StopFinder(
    private val stopRepository: StopRepository
) {
    fun findByPlaceId(placeId: Long): List<Stop> {
        return stopRepository.findByPlaceId(placeId)
    }
}
