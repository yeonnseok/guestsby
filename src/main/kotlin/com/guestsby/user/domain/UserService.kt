package com.guestsby.user.domain

import com.guestsby.auth.domain.dto.EmailCheckRequest
import com.guestsby.auth.domain.dto.SignupRequest
import com.guestsby.user.controller.request.UserUpdateRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userValidator: UserValidator,
    private val userCreator: UserCreator,
    private val userFinder: UserFinder,
    private val userUpdater: UserUpdater,
    private val userDeleter: UserDeleter
) {
    fun create(request: SignupRequest): Long {
        userValidator.validate(request)

        val user = userCreator.create(request)
        return user.id!!
    }

    fun find(id: Long): User {
        return userFinder.findById(id)
    }

    fun update(id: Long, request: UserUpdateRequest) {
        userUpdater.update(id, request)
    }

    fun delete(id: Long) {
        userDeleter.delete(id)
    }

    fun checkEmail(request: EmailCheckRequest): Boolean {
        return userValidator.checkEmail(request.email)
    }
}
