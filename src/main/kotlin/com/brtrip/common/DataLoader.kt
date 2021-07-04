package com.brtrip.common

import com.brtrip.user.domain.AuthProvider
import com.brtrip.user.domain.RoleType
import com.brtrip.user.domain.User
import com.brtrip.user.domain.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class DataLoader(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
) : ApplicationRunner {
    override fun run(args: ApplicationArguments) {

        // Save Sample User
        userRepository.save(
            User(
                nickName = "admin",
                email = "test@.com",
                password = passwordEncoder.encode("test"),
                role = RoleType.ROLE_ADMIN,
                authProvider = AuthProvider.LOCAL
            )
        )
    }
}