package com.brtrip.favorite.domain

import org.springframework.data.jpa.repository.JpaRepository

interface FavoriteRepository : JpaRepository<Favorite, Long> {
    fun findByUserIdAndDeleted(userId: Long, deleted: Boolean): List<Favorite>
}