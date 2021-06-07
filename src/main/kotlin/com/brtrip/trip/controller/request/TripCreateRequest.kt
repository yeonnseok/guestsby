package com.brtrip.trip.controller.request

import com.brtrip.common.utils.yyyy_MM_dd_Formatter
import com.brtrip.trip.domain.Trip

data class TripCreateRequest(
    val title: String,
    val stops: List<StopCreateRequest>,
    val startDate: String,
    val endDate: String,
    val memo: String? = null
) {
    fun toEntity(userId: Long): Trip {
        return Trip(
            userId = userId,
            title = title,
            startDate = startDate.yyyy_MM_dd_Formatter(),
            endDate = endDate.yyyy_MM_dd_Formatter(),
            memo = memo
        )
    }
}
