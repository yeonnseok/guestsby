package com.guestsby.user.domain

import com.guestsby.auth.domain.dto.SignupRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class UserValidator(
    private val userRepository: UserRepository
) {
    fun validate(request: SignupRequest) {
        validateUserEmail(request.email)
        validatePassword(request.password, request.passwordCheck)
    }

    private fun validateUserEmail(email: String) {
        if (userRepository.existsByEmail(email)) {
            throw RuntimeException("이미 존재하는 이메일입니다.")
        }
    }

    private fun validatePassword(password: String, passwordCheck: String) {
        if (password != passwordCheck) {
            throw RuntimeException("패스워드 확인이 필요합니다.")
        }
    }

    fun checkEmail(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }
}
