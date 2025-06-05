package com.guestsby.user.domain

import com.guestsby.auth.domain.dto.SignupRequest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class UserCreator(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository
) {
    fun create(request: SignupRequest): User {
        val encodedPw = passwordEncoder.encode(request.password)
        return userRepository.save(request.toEntity(encodedPw))
    }
}
