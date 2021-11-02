package com.brtrip.user.domain

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class UserDeleter(
    private val userFinder: UserFinder
) {
    fun delete(id: Long) {
        val user = userFinder.findById(id)
        user.delete()
    }
}