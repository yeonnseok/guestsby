package com.brtrip.path.controller.response

import com.brtrip.path.domain.Path
import com.brtrip.place.Place
import com.brtrip.place.controller.response.PlaceResponse

class PathResponse(
    val id: Long,
    val places: List<PlaceResponse>,
    val likeCount: Long,
    val name: String
) {
    companion object {
        @JvmStatic fun of(path: Path, places: List<Place>): PathResponse {
            return PathResponse(
                id = path.id!!,
                places = places.map { PlaceResponse.of(it) },
                likeCount = path.likeCount,
                name = path.name
            )
        }
    }

    override fun toString(): String = "id: " + id.toString() +
            ", places: " + places + ", likeCount: " + likeCount + ", name: " + name
}
