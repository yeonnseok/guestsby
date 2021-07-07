package com.brtrip.trip.controller.response

import com.brtrip.trip.domain.Stop
import java.math.BigDecimal

data class StopResponse(
    val name: String,
    val lat: String,
    val lng: String,
    val sequence: Int
) {
    companion object {
        fun of(stop: Stop): StopResponse {
            return StopResponse(
                name = stop.place.name,
                lat = stop.place.lat,
                lng = stop.place.lng,
                sequence = stop.sequence
            )
        }
    }
}
