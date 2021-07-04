package com.brtrip.trip.domain

import com.brtrip.trip.controller.request.TripRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.stream.IntStream
import kotlin.streams.toList

@Component
@Transactional
class TripCreator(
    private val tripRepository: TripRepository,
    private val stopCreator: StopCreator
) {
    fun create(userId: Long, request: TripRequest): Trip {
        val trip = tripRepository.save(request.toEntity(userId))

        val stops = IntStream.range(0, request.stops.size)
            .mapToObj {
                stopCreator.create(trip, request.stops, it)
            }
            .toList() as MutableList

        trip.stops = stops
        return trip
    }
}
