package com.brtrip.path.controller

import com.brtrip.auth.domain.UserPrincipal
import com.brtrip.common.response.ApiResponse
import com.brtrip.path.controller.request.PathRequest
import com.brtrip.path.domain.PathService
import com.brtrip.trip.controller.request.TripRequest
import com.brtrip.user.domain.LoginUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/paths")
class PathController(
    private val pathService: PathService
) {
    @PostMapping
    fun create(
        @Valid @RequestBody request: PathRequest
    ): ResponseEntity<Void> {
        val pathId = pathService.create(request)

        val location = ServletUriComponentsBuilder
            .fromCurrentContextPath().path("/api/v1/paths/$pathId")
            .buildAndExpand(pathId).toUri()

        return ResponseEntity.created(location).build()
    }

    @GetMapping
    fun recommend(
        @RequestParam lat: String,
        @RequestParam lng: String
    ): ResponseEntity<ApiResponse> {
        val paths = pathService.recommendPaths(lat, lng)
        return ResponseEntity
            .ok(
                ApiResponse(
                    data = paths
                )
            )
    }
}