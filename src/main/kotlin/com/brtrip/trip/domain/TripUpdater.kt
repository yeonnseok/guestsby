package com.brtrip.trip.domain

import com.brtrip.trip.controller.request.TripRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.stream.IntStream
import kotlin.streams.toList

@Component
@Transactional
class TripUpdater(
    private val tripFinder: TripFinder,
    private val stopRepository: StopRepository
) {
    fun update(tripId: Long, request: TripRequest) {
        val trip = tripFinder.findById(tripId)

        trip.title = request.title
        trip.memo = request.memo

        trip.stops = IntStream.range(0, request.stops.size)
            .mapToObj {
                val stop = request.stops[it]
                    .toEntity(trip, it + 1)
                stopRepository.save(stop)
            }
            .toList() as MutableList
    }
}
