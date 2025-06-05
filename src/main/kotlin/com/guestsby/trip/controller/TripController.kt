package com.guestsby.trip.controller

import com.guestsby.auth.domain.UserPrincipal
import com.guestsby.common.response.ApiResponse
import com.guestsby.common.response.ResultType
import com.guestsby.trip.controller.request.TripRequest
import com.guestsby.trip.controller.response.TripCreateResponse
import com.guestsby.trip.domain.TripService
import com.guestsby.user.domain.LoginUser
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/trips")
class TripController(
    private val tripService: TripService
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping
    fun create(
        @LoginUser userPrincipal: UserPrincipal,
        @Valid @RequestBody request: TripRequest
    ): ResponseEntity<ApiResponse> {
        val tripId = tripService.create(userPrincipal.getId(), request)

        val location = ServletUriComponentsBuilder
            .fromCurrentContextPath().path("/api/v1/trips/$tripId")
            .buildAndExpand(tripId).toUri()

        return ResponseEntity.created(location).body(
            ApiResponse(
                statusCode = 201,
                data = TripCreateResponse(tripId)
            )
        )
    }

    @GetMapping("/my")
    fun findMyTrips(
        @LoginUser userPrincipal: UserPrincipal
    ): ResponseEntity<ApiResponse> {
        val trips = tripService.findMyTrips(userPrincipal.getId())
        return ResponseEntity
            .ok(
                ApiResponse(
                    data = trips
                )
            )
    }

    @PatchMapping("/{id}")
    fun update(
        @LoginUser userPrincipal: UserPrincipal,
        @PathVariable id: Long,
        @Valid @RequestBody request: TripRequest
    ): ResponseEntity<ApiResponse> {
        log.info("update start - {}", id)
        val updated = tripService.update(userPrincipal.getId(), id, request)
        log.info("trip response - {}", updated)
        log.info("tripPath - {}", updated.paths)
        return ResponseEntity.ok(ApiResponse(data = updated))
    }

    @DeleteMapping("/{id}")
    fun delete(
        @LoginUser userPrincipal: UserPrincipal,
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse> {
        tripService.delete(userPrincipal.getId(), id)
        return ResponseEntity.ok(ApiResponse(data = null))
    }
}
