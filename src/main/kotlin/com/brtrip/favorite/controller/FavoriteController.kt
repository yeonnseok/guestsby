package com.brtrip.favorite.controller

import com.brtrip.favorite.domain.FavoriteService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/likes")
class FavoriteController(
    private val favoriteService: FavoriteService
) {
}