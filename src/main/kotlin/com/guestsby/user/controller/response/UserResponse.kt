package com.guestsby.user.controller.response

import com.guestsby.user.domain.User

data class UserResponse(
    val nickName: String,
    val email: String,
    val role: String
) {
    companion object {
        fun of(user: User): UserResponse {
            return UserResponse(
                nickName = user.nickName!!,
                email = user.email,
                role = user.role.text
            )
        }
    }
}
