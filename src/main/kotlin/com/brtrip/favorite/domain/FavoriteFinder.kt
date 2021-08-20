package com.brtrip.favorite.domain

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class FavoriteFinder(
    private val favoriteRepository: FavoriteRepository
) {
    fun findByUserId(userId: Long): List<Favorite> {
        return favoriteRepository.findByUserIdAndDeleted(userId, false)
    }
}