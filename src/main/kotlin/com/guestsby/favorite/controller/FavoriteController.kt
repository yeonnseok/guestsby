package com.guestsby.favorite.controller

import com.guestsby.auth.domain.UserPrincipal
import com.guestsby.common.response.ApiResponse
import com.guestsby.favorite.controller.request.FavoriteRequest
import com.guestsby.favorite.controller.response.FavoriteCreateResponse
import com.guestsby.favorite.domain.FavoriteService
import com.guestsby.trip.controller.request.TripRequest
import com.guestsby.trip.controller.response.TripCreateResponse
import com.guestsby.user.domain.LoginUser
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
