package com.brtrip

import com.brtrip.place.Place
import com.brtrip.user.domain.AuthProvider
import com.brtrip.user.domain.RoleType
import com.brtrip.user.domain.User
import com.brtrip.user.domain.UserRepository
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
