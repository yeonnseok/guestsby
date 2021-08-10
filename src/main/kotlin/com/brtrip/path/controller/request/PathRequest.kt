package com.brtrip.path.controller.request

import com.brtrip.path.Path
import com.brtrip.place.Place

class PathRequest(
    val id: Long?,
    val places: List<Place>
) {
    fun toEntity(): Path {
        return Path()
    }
}