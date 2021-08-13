package com.brtrip.path.controller.request

import com.brtrip.path.Path
import com.brtrip.place.PlaceRequest

class PathRequest(
    val id: Long,
    val places: List<PlaceRequest>
) {
    fun toEntity(): Path {
        return Path()
    }
}
