package com.brtrip.trip.controller.request

import com.brtrip.place.Place
import com.brtrip.trip.domain.Stop
import com.brtrip.trip.domain.Trip

data class StopRequest(
    val lat: Long,
    val lng: Long,
    val name: String
) {
    fun toEntity(trip: Trip, sequence: Int): Stop {
        return Stop(
            trip = trip,
            place = Place(
                lat = lat,
                lng = lng,
                name = name
            ),
            sequence = sequence
        )
    }
}
