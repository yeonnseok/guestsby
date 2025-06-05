package com.guestsby.path.controller.response

import com.guestsby.path.domain.Path
import com.guestsby.place.Place
import com.guestsby.place.dto.PlaceResponse

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
