package com.brtrip.place

data class PlaceRequest(
    var lat: String,
    var lng: String,
    var name: String? = null,
    var content: String? = null
)  {
    fun toEntity(): Place {
        return Place(
            lat = lat,
            lng = lng,
            name = name,
            content = content
        )
    }
}
