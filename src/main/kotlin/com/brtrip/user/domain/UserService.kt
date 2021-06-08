package com.brtrip.user.domain

import com.brtrip.user.controller.request.UserUpdateRequest
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userFinder: UserFinder,
    private val userUpdater: UserUpdater,
    private val userDeleter: UserDeleter
) {
    fun find(id: Long): User {
        return userFinder.findById(id)
    }

    fun update(id: Long, request: UserUpdateRequest) {
        userUpdater.update(id, request)
    }

    fun delete(id: Long) {
        userDeleter.delete(id)
    }
}