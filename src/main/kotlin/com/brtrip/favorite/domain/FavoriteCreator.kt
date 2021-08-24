package com.brtrip.favorite.domain

import com.brtrip.favorite.controller.request.FavoriteRequest
import com.brtrip.path.domain.PathFinder
import com.brtrip.user.domain.UserFinder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class FavoriteCreator(
    private val favoriteRepository: FavoriteRepository,
    private val userFinder: UserFinder,
    private val pathFinder: PathFinder
) {
    fun create(userId: Long, request: FavoriteRequest): Favorite {
        val user = userFinder.findById(userId)
        val path = pathFinder.findById(request.pathId)

        return favoriteRepository.save(request.toEntity(user, path))
    }
}