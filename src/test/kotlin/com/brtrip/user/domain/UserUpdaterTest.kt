package com.brtrip.user.domain

import com.brtrip.common.exceptions.NotFoundException
import com.brtrip.user.controller.request.UserUpdateRequest
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
@Sql("/truncate.sql")
internal class UserUpdaterTest {

    @Autowired
    private lateinit var sut: UserUpdater

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `유저 정보 수정`() {
        // given
        val user = userRepository.save(
            User(
                nickName = "여행가",
                email = "trip@com",
                role = RoleType.ROLE_USER,
                authProvider = AuthProvider.KAKAO
            )
        )
        val request = UserUpdateRequest(nickName = "여행 매니아")

        // when
        sut.update(user.id!!, request)
        val result = userRepository.findById(user.id!!)
            .orElseThrow { NotFoundException("유저가 존재하지 않습니다.") }

        // then
        result.nickName shouldBe request.nickName
    }

}
