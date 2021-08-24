package com.brtrip.favorite.domain

import com.brtrip.favorite.controller.request.FavoriteRequest
import com.brtrip.path.controller.response.PathResponse
import com.brtrip.user.domain.UserFinder
import org.springframework.stereotype.Service

@Service
class FavoriteService(
    private val favoriteFinder: FavoriteFinder,
    private val favoriteCreator: FavoriteCreator,
    private val favoriteDeleter: FavoriteDeleter,
    private val userFinder: UserFinder
) {
    fun findMyFavorites(userId: Long): List<PathResponse> {
        val favorites = favoriteFinder.findByUser(userFinder.findById(userId))
        return favorites.map {
            val places = it.path.pathPlaces.map { it.place }
            PathResponse.of(it.path, places)
        }
    }

    fun create(userId: Long, request: FavoriteRequest): Long {
        return favoriteCreator.create(userId, request).id!!
    }

    fun delete(userId: Long, favoriteId: Long) {
        favoriteDeleter.delete(favoriteId)
    }
}