package com.brtrip.place

import java.util.stream.IntStream

data class PlaceRequest(
    var lat: String,
    var lng: String,
    var name: String,
    var content: String? = null,
    var keywords: Array<String?>
) {
    fun toEntity(): Place {
        var place = Place(
            lat = lat,
            lng = lng,
            name = name,
            content = content,
        )
        val placeCategories = mutableListOf<PlaceCategory>()
        IntStream.range(0, keywords.size).forEach {
            placeCategories.add(PlaceCategory(null, Category(null, keywords[it]!!), place, false))
        }
        placeCategories.also { place.placeCategories = it }

        return place
    }
}
