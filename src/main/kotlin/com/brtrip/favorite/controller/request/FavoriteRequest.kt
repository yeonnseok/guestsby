package com.brtrip.favorite.controller.request

import com.brtrip.favorite.domain.Favorite
import com.brtrip.path.Path
import com.brtrip.user.domain.User

class FavoriteRequest(
    val path: Path
) {
    fun toEntity(user: User): Favorite {
        return Favorite(
            user = user,
            path = path
        )
    }
}