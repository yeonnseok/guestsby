package com.brtrip.trip.domain

import com.brtrip.place.PlaceRepository
import com.brtrip.trip.controller.request.StopRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class StopCreator(
    private val stopRepository: StopRepository,
    private val placeRepository: PlaceRepository
) {
    fun create(trip: Trip, request: List<StopRequest>, index: Int): Stop {
        val stop = request[index]
            .toEntity(trip, index + 1)

        setPlace(stop)
        return stopRepository.save(stop)
    }

    private fun setPlace(stop: Stop) {
        val place = placeRepository.findByLatAndLng(
            stop.place.lat,
            stop.place.lng
        )

        if (place != null) {
            stop.place = place
        } else {
            stop.place = placeRepository.save(stop.place)
        }
    }
}
