package com.brtrip.trip.controller.response

import com.brtrip.common.utils.format_yyyy_MM_dd
import com.brtrip.path.controller.response.PathResponse
import com.brtrip.trip.domain.Trip

data class TripResponse(
    val id: Long,
    val title: String,
    val startDate: String,
    val endDate: String,
    val memo: String?,
    val paths: List<PathResponse>
) {
    companion object {
        fun of(trip: Trip): TripResponse {
            val paths = trip.tripPaths.map { it.path }
            val places = paths.map { path -> path.pathPlaces.map { it.place } }
            return TripResponse(
                id = trip.id!!,
                title = trip.title,
                startDate = trip.startDate.format_yyyy_MM_dd(),
                endDate = trip.endDate.format_yyyy_MM_dd(),
                memo = trip.memo,
                paths = paths.mapIndexed { index, path ->
                    PathResponse.of(path, places[index])
                }
            )
        }
    }
}
