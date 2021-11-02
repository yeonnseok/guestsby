package com.brtrip.place.controller.response

import com.brtrip.place.Place

data class PlaceDetailResponse(
    val lat: String,
    val lng: String,
    val name: String,
    val content: String?
) {
    companion object {
        fun of(place: Place): PlaceDetailResponse {
            return PlaceDetailResponse(
                lat = place.lat,
                lng = place.lng,
                name = place.name,
                content = place.content
            )
        }
    }
}
