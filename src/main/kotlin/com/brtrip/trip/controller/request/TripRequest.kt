package com.brtrip.trip.controller.request

import com.brtrip.common.utils.yyyy_MM_dd_Formatter
import com.brtrip.trip.domain.Trip

data class TripRequest(
    val title: String,
    val stops: List<StopRequest>,
    val memo: String? = null
) {
    fun toEntity(userId: Long): Trip {
        return Trip(
            userId = userId,
            title = title,
            memo = memo
        )
    }
}
