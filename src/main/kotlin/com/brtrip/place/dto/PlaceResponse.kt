package com.brtrip.place.dto

import com.brtrip.place.Place

data class PlaceResponse(
    val lat: String,
    val lng: String,
    val name: String?
) {
    companion object {
        fun of(place: Place): PlaceResponse {
            return PlaceResponse(
                lat = place.lat,
                lng = place.lng,
                name = place.name
            )
        }
    }
}
