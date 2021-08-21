package com.brtrip.place

import org.springframework.stereotype.Service

@Service
class PlaceService(
    private val placeRepository: PlaceRepository
) {

}
