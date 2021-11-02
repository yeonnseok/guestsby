package com.brtrip.place

import com.brtrip.place.dto.PlaceDetailResponse
import org.springframework.stereotype.Service

@Service
class PlaceService(
    private val placeFinder: PlaceFinder
) {
    fun findById(id: Long): PlaceDetailResponse {
        return PlaceDetailResponse.of(placeFinder.findById(id))
    }
}
