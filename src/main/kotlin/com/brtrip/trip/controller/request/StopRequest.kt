package com.brtrip.trip.controller.request

import com.brtrip.place.Place
import com.brtrip.trip.domain.Stop
import com.brtrip.trip.domain.Trip
import java.math.BigDecimal

data class StopRequest(
    val lat: String,
    val lng: String,
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
