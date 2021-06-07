package com.brtrip.trip.controller

import com.brtrip.auth.domain.UserPrincipal
import com.brtrip.trip.controller.request.TripCreateRequest
import com.brtrip.trip.domain.TripService
import com.brtrip.user.domain.LoginUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/trips")
class TripController(
    private val tripService: TripService
) {
    @PostMapping
    fun create(
            @LoginUser userPrincipal: UserPrincipal,
            @Valid @RequestBody request: TripCreateRequest
    ): ResponseEntity<Void> {
        val tripId = tripService.create(userPrincipal.getId(), request)

        val location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/v1/trips/$tripId")
                .buildAndExpand(tripId).toUri()

        return ResponseEntity.created(location).build()
    }
}
