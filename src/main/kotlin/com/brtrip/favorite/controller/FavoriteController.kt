package com.brtrip.favorite.controller

import com.brtrip.auth.domain.UserPrincipal
import com.brtrip.common.response.ApiResponse
import com.brtrip.favorite.domain.FavoriteService
import com.brtrip.user.domain.LoginUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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
}