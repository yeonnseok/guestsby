package com.guestsby

import com.guestsby.user.domain.AuthProvider
import com.guestsby.user.domain.RoleType
import com.guestsby.user.domain.User
import com.guestsby.user.domain.UserRepository
import org.springframework.stereotype.Component
import org.springframework.test.context.ActiveProfiles

@Component
@ActiveProfiles("test")
internal class TestDataLoader(
    private val userRepository: UserRepository
) {
    fun sample_user() = userRepository.save(
        User(
            id = 1L,
            nickName = "여행가",
            email = "trip@com",
            password = "test123",
            role = RoleType.ROLE_USER,
            authProvider = AuthProvider.KAKAO
        )
    )
}
