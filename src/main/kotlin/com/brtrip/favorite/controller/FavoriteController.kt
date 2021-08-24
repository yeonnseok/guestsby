package com.brtrip.favorite.controller

import com.brtrip.auth.domain.UserPrincipal
import com.brtrip.common.response.ApiResponse
import com.brtrip.favorite.controller.request.FavoriteRequest
import com.brtrip.favorite.controller.response.FavoriteCreateResponse
import com.brtrip.favorite.domain.FavoriteService
import com.brtrip.trip.controller.request.TripRequest
import com.brtrip.trip.controller.response.TripCreateResponse
import com.brtrip.user.domain.LoginUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/favorites")
class FavoriteController(
    private val favoriteService: FavoriteService
) {
    @GetMapping
    fun findMyFavorite(
        @LoginUser userPrincipal: UserPrincipal
    ): ResponseEntity<ApiResponse> {
        val favoritePaths = favoriteService.findMyFavorites(userPrincipal.getId())
        return ResponseEntity
            .ok(
                ApiResponse(
                    data = favoritePaths
                )
            )
    }

    @PostMapping
    fun create(
        @LoginUser userPrincipal: UserPrincipal,
        @Valid @RequestBody request: FavoriteRequest
    ): ResponseEntity<ApiResponse> {
        val favoriteId = favoriteService.create(userPrincipal.getId(), request)

        val location = ServletUriComponentsBuilder
            .fromCurrentContextPath().path("/api/v1/favorites/$favoriteId")
            .buildAndExpand(favoriteId).toUri()

        return ResponseEntity.created(location).body(
            ApiResponse(
                statusCode = 201,
                data = FavoriteCreateResponse(favoriteId)
            )
        )
    }

    @DeleteMapping("/{id}")
    fun delete(
        @LoginUser userPrincipal: UserPrincipal,
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse> {
        favoriteService.delete(userPrincipal.getId(), id)
        return ResponseEntity.ok(ApiResponse(data = null))
    }
}