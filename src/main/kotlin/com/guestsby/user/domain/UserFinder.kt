package com.guestsby.user.domain

import com.guestsby.common.exceptions.NotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class UserFinder(
    private val userRepository: UserRepository
) {
    fun findById(userId: Long): User {
        return userRepository.findByIdAndDeleted(userId, false)
            ?: throw NotFoundException("존재 하지 않는 유저 입니다.")
    }
}
