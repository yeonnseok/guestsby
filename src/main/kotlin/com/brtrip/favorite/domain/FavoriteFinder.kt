package com.brtrip.favorite.domain

import com.brtrip.common.exceptions.NotFoundException
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

    fun findById(favoriteId: Long): Favorite {
        return favoriteRepository.findByIdAndDeleted(favoriteId, false)
            ?: throw NotFoundException("찜한 경로가 없습니다.")
    }
}