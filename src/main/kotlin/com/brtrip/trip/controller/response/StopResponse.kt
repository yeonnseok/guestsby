package com.brtrip.trip.controller.response

import com.brtrip.trip.domain.Stop

data class StopResponse(
    val name: String,
    val lat: Long,
    val lng: Long,
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
