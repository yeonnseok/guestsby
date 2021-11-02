package com.brtrip.user.domain

import com.brtrip.user.controller.request.UserUpdateRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class UserUpdater(
    private val userFinder: UserFinder
) {
    fun update(userId: Long, request: UserUpdateRequest) {
        val user = userFinder.findById(userId)
        user.nickName = request.nickName
    }
}