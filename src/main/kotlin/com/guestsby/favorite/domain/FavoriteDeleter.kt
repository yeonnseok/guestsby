package com.guestsby.favorite.domain

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class FavoriteDeleter(
    private val favoriteFinder: FavoriteFinder
) {
    fun delete(favoriteId: Long) {
        val favorite = favoriteFinder.findById(favoriteId)
        favorite.delete()
    }
}
