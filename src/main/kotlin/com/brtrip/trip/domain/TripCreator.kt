package com.brtrip.trip.domain

import com.brtrip.trip.controller.request.TripCreateRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.stream.IntStream
import kotlin.streams.toList

@Component
@Transactional
class TripCreator(
    private val tripRepository: TripRepository,
    private val stopRepository: StopRepository
) {
    fun create(userId: Long, request: TripCreateRequest): Trip {
        val trip = tripRepository.save(request.toEntity(userId))

        val stops = IntStream.range(0, request.stops.size)
            .mapToObj {
                val stop = request.stops[it]
                    .toEntity(trip, it + 1)
                stopRepository.save(stop)
            }
            .toList()

        trip.stops = stops
        return trip
    }
}
