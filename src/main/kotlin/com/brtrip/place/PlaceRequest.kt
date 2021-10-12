package com.brtrip.place

import com.brtrip.place.Place

data class PlaceRequest(
    var lat: String,
    var lng: String,
    var name: String,
    var content: String? = null,
    var keywords: Array<String?>
) {
    fun toEntity(): Place {
        return Place(
            lat = lat,
            lng = lng,
            name = name,
            content = content
        )
    }
}
