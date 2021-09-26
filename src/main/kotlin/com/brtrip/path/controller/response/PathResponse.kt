package com.brtrip.path.controller.response

import com.brtrip.path.domain.Path
import com.brtrip.place.Place
import com.brtrip.place.dto.PlaceResponse

class PathResponse(
    val places: List<PlaceResponse>,
    val likeCount: Long,
    val name: String
) {
    companion object {
        fun of(path: Path, places: List<Place>): PathResponse {
            return PathResponse(
                places = places.map { PlaceResponse.of(it) },
                likeCount = path.likeCount,
                name = path.name
            )
        }
    }
}
