package com.brtrip.place.dto

import com.brtrip.place.Place

data class PlaceResponse(
    val lat: String,
    val lng: String,
    val name: String?,
    val keywords: Array<String?>
) {
    companion object {
        fun of(place: Place): PlaceResponse {
            return PlaceResponse(
                lat = place.lat,
                lng = place.lng,
                name = place.name,
                keywords = place.placeCategories.map { it.category.name }.toTypedArray()
            )
        }
    }
}
