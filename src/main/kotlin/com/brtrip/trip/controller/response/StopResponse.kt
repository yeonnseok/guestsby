package com.brtrip.trip.controller.response

import com.brtrip.common.utils.format_yyyy_MM_dd_hh_mm_ss
import com.brtrip.trip.domain.Stop

data class StopResponse(
    val name: String,
    val lat: Long,
    val lng: Long,
    val stoppedAt: String,
    val sequence: Int
) {
    companion object {
        fun of(stop: Stop): StopResponse {
            return StopResponse(
                name = stop.name,
                lat = stop.lat,
                lng = stop.lng,
                stoppedAt = stop.stoppedAt.format_yyyy_MM_dd_hh_mm_ss(),
                sequence = stop.sequence
            )
        }
    }
}
