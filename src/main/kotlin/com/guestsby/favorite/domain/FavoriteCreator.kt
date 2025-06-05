package com.guestsby.favorite.domain

import com.guestsby.favorite.controller.request.FavoriteRequest
import com.guestsby.path.domain.PathFinder
import com.guestsby.user.domain.UserFinder
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
