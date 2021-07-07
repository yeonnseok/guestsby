package com.brtrip.user.domain

import com.brtrip.TestDataLoader
import com.brtrip.common.exceptions.NotFoundException
import com.brtrip.user.controller.request.UserUpdateRequest
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
@Sql("/truncate.sql")
internal class UserServiceTest {

    @Autowired
    private lateinit var sut: UserService

    @Autowired
    private lateinit var testDataLoader: TestDataLoader

    private var user: User? = null

    @BeforeEach
    fun setUp() {
        user = testDataLoader.sample_user()
    }

    @Test
    fun `유저 조회`() {
        // when
        val result = sut.find(user!!.id!!)

        // then
        result shouldBe user
    }

    @Test
    fun `유저 수정`() {
        // given
        val request = UserUpdateRequest(nickName = "여행 매니아")

        // when
        sut.update(user!!.id!!, request)
        val result = sut.find(user!!.id!!)

        // then
        result.nickName shouldBe request.nickName
    }

    @Test
    fun `유저 삭제`() {
        // when
        sut.delete(user!!.id!!)

        // then
        shouldThrow<NotFoundException> { sut.find(user!!.id!!) }
    }
}