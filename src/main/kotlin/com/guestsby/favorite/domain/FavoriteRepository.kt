package com.guestsby.favorite.domain

import com.guestsby.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface FavoriteRepository : JpaRepository<Favorite, Long> {
    fun findByUserAndDeleted(user: User, deleted: Boolean): List<Favorite>
    fun findByIdAndDeleted(favoriteId: Long, deleted: Boolean): Favorite
}
