package com.brtrip.user.domain

import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
@Sql("/truncate.sql")
internal class UserFinderTest {

    @Autowired
    private lateinit var sut: UserFinder

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `유저 정보 조회`() {
        val user = userRepository.save(
            User(
                nickName = "여행가",
                email = "trip@com",
                role = RoleType.ROLE_USER,
                password = "test123",
                authProvider = AuthProvider.KAKAO
            )
        )

        // when
        val result = sut.findById(user.id!!)

        // then
        result.nickName shouldBe user.nickName
        result.email shouldBe user.email
        result.role shouldBe user.role
        result.authProvider shouldBe user.authProvider
    }
}