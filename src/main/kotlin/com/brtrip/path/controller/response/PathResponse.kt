package com.brtrip.path.controller.response

import com.brtrip.path.Path
import com.brtrip.place.Place
import com.brtrip.place.dto.PlaceResponse

class PathResponse(
    val places: List<PlaceResponse>,
    val likeCount: Long
) {
    companion object {
        fun of(path: Path, places: List<Place>): PathResponse {
            return PathResponse(
                places = places.map { PlaceResponse.of(it) },
                likeCount = path.likeCount
            )
        }
    }
}
