package com.brtrip.favorite.domain

import com.brtrip.path.controller.response.PathResponse
import org.springframework.stereotype.Service

@Service
class FavoriteService(
    private val favoriteFinder: FavoriteFinder
) {
    fun findMyFavorites(userId: Long): List<PathResponse> {
        val favorites = favoriteFinder.findByUserId(userId)
        return favorites.map {
            val places = it.path.pathPlaces.map { it.place }
            PathResponse.of(it.path, places)
        }
    }
}