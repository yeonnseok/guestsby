package com.brtrip.place

class Places(
    places: List<Place>
) {
    private var places: List<Place> = listOf()
    init {
        this.places = places
    }

    fun isChanged(placeRequests: List<PlaceRequest>): Boolean {
        if (placeRequests.size != places.size) return true

        places.forEachIndexed { index, savedPlace ->
            if (placeRequests[index].lat != savedPlace.lat ||
                placeRequests[index].lng != savedPlace.lng) return true
        }

        return false
    }
}