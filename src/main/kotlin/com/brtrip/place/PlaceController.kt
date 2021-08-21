package com.brtrip.place

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/places")
class PlaceController(
    private val placeService: PlaceService
) {
    fun create(request: PlaceRequest) {

    }
}
