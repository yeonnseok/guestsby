package com.brtrip.favorite.domain

import com.brtrip.user.domain.User
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class FavoriteFinder(
    private val favoriteRepository: FavoriteRepository
) {
    fun findByUser(user: User): List<Favorite> {
        return favoriteRepository.findByUserAndDeleted(user, false)
    }

    fun findById(favoriteId: Long): Favorite {
        return favoriteRepository.findByIdAndDeleted(favoriteId, false)
    }
}