package com.guestsby.favorite.controller.request

import com.guestsby.favorite.domain.Favorite
import com.guestsby.path.domain.Path
import com.guestsby.user.domain.User

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
