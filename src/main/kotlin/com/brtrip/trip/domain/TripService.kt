package com.brtrip.trip.domain

import com.brtrip.trip.controller.request.TripCreateRequest
import org.springframework.stereotype.Service

@Service
class TripService(
    private val tripCreator: TripCreator
) {
    fun create(userId: Long, request: TripCreateRequest): Long {
        val trip = tripCreator.create(userId, request)
        return trip.id!!
    }
}
