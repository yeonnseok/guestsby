package com.brtrip.auth.domain


import com.brtrip.common.exceptions.NotFoundException
import com.brtrip.user.domain.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CustomUserDetailsService(
    private val userRepository: UserRepository
): UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmail(email)
        if (user != null) {
            return UserPrincipal.of(user)
        }
        throw NotFoundException("존재하지 않는 User 입니다.")
    }

    fun loadUserById(id: Long): UserDetails {
        val user = userRepository.findById(id)
            .orElseThrow { NotFoundException("존재하지 않는 User 입니다.") }

        return UserPrincipal.of(user)
    }
}
