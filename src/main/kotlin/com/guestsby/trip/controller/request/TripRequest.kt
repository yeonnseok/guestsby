package com.guestsby.trip.controller.request

import com.guestsby.common.utils.yyyy_MM_dd_Formatter
import com.guestsby.path.controller.request.PathRequest
import com.guestsby.trip.domain.Trip

data class TripRequest(
    val title: String,
    val startDate: String,
    val endDate: String,
    val memo: String? = null,
    val paths: List<PathRequest>
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
