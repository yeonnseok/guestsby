package com.brtrip.trip.controller.request

import com.brtrip.common.utils.yyyy_MM_dd_HH_mm_SS_Formatter
import com.brtrip.trip.domain.Stop
import com.brtrip.trip.domain.Trip

data class StopCreateRequest(
    val lat: Long,
    val lng: Long,
    val name: String,
    val stoppedAt: String
) {
    fun toEntity(trip: Trip, sequence: Int): Stop {
        return Stop(
            trip = trip,
            lat = lat,
            lng = lng,
            name = name,
            stoppedAt = stoppedAt.yyyy_MM_dd_HH_mm_SS_Formatter(),
            sequence = sequence
        )
    }
}
