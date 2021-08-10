package com.brtrip.trip.domain

import com.brtrip.trip.controller.request.TripRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.stream.IntStream
import kotlin.streams.toList

@Component
@Transactional
class TripCreator(
    private val tripRepository: TripRepository
) {
    fun create(userId: Long, request: TripRequest): Trip {
        return tripRepository.save(request.toEntity(userId))
    }
}
