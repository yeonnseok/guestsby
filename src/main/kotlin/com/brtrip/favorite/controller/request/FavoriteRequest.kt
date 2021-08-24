package com.brtrip.favorite.controller.request

import com.brtrip.favorite.domain.Favorite
import com.brtrip.path.domain.Path
import com.brtrip.user.domain.User

class FavoriteRequest(
    val pathId: Long
) {
    fun toEntity(user: User, path: Path): Favorite {
        return Favorite(
            user = user,
            path = path
        )
    }
}