package com.guestsby.place

class Places(
    val places: List<Place>
) {
    fun isChanged(placeRequests: List<PlaceRequest>): Boolean {
        if (placeRequests.size != places.size) return true

        places.forEachIndexed { index, savedPlace ->
            if (placeRequests[index].lat != savedPlace.lat ||
                placeRequests[index].lng != savedPlace.lng) return true
        }

        return false
    }
}
