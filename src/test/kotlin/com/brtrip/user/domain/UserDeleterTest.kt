package com.brtrip.user.domain

import com.brtrip.common.exceptions.NotFoundException
import io.kotlintest.shouldThrow
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
@Sql("/truncate.sql")
internal class UserDeleterTest {

    @Autowired
    private lateinit var sut: UserDeleter

    @Autowired
    private lateinit var userFinder: UserFinder

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `유저 삭제`() {
        // given
        val user = userRepository.save(
            User(
                nickName = "여행가",
                email = "trip@com",
                password = "test123",
                role = RoleType.ROLE_USER,
                authProvider = AuthProvider.KAKAO
            )
        )

        // when
        sut.delete(user.id!!)

        // then
        shouldThrow<NotFoundException> { userFinder.findById(user.id!!) }
    }

}
