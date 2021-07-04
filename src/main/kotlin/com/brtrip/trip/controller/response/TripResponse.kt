package com.brtrip.trip.controller.response

import com.brtrip.common.utils.format_yyyy_MM_dd
import com.brtrip.trip.domain.Trip

data class TripResponse(
    val title: String,
    val stops: List<StopResponse>,
    val memo: String?
) {
    companion object {
        fun of(trip: Trip): TripResponse {
            return TripResponse(
                title = trip.title,
                stops = trip.stops.map { StopResponse.of(it) },
                memo = trip.memo
            )
        }
    }
}
