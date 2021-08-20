package com.brtrip.favorite.domain

import com.brtrip.favorite.controller.request.FavoriteRequest
import com.brtrip.path.controller.response.PathResponse
import com.brtrip.user.domain.User
import org.springframework.stereotype.Service

@Service
class FavoriteService(
    private val favoriteFinder: FavoriteFinder,
    private val favoriteCreator: FavoriteCreator
) {
    fun findMyFavorites(userId: Long): List<PathResponse> {
        val favorites = favoriteFinder.findByUserId(userId)
        return favorites.map {
            val places = it.path.pathPlaces.map { it.place }
            PathResponse.of(it.path, places)
        }
    }

    fun create(userId: Long, request: FavoriteRequest): Long {
        return favoriteCreator.create(userId, request).id!!
    }
}