package com.brtrip.trip.domain

import com.brtrip.path.domain.PathCreator
import com.brtrip.trip.controller.request.TripRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.stream.IntStream

@Component
@Transactional
class TripCreator(
    private val tripRepository: TripRepository,
    private val tripPathRepository: TripPathRepository,
    private val pathCreator: PathCreator
) {
    fun create(userId: Long, request: TripRequest): Trip {
        val trip = tripRepository.save(request.toEntity(userId))

        IntStream.range(0, request.paths.size)
            .mapToObj {
                val path = pathCreator.createPathWithinTrip(request.paths, it)
                tripPathRepository.save(TripPath(
                    trip = trip,
                    path = path
                ))
            }

        return trip
    }
}
