package com.guestsby.place.controller

import com.guestsby.common.response.ApiResponse
import com.guestsby.place.PlaceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/places")
class PlaceController(
    private val placeService: PlaceService
) {
    @GetMapping("/{id}")
    fun find(@PathVariable id: Long): ResponseEntity<ApiResponse> {
        val place = placeService.findById(id)
        return ResponseEntity.ok(
            ApiResponse(
                data = place
            )
        )
    }
}
