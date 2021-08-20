package com.brtrip.favorite.domain

import com.brtrip.common.exceptions.NotFoundException
import com.brtrip.favorite.controller.request.FavoriteRequest
import com.brtrip.user.domain.UserRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class FavoriteCreator(
    private val favoriteRepository: FavoriteRepository,
    private val userRepository: UserRepository
) {
    fun create(userId: Long, request: FavoriteRequest): Favorite {
        val user = userRepository.findById(userId)
            .orElseThrow { NotFoundException("유저를 찾을 수 없습니다.") }

        return favoriteRepository.save(request.toEntity(user))
    }
}